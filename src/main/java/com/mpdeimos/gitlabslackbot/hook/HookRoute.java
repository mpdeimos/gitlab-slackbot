package com.mpdeimos.gitlabslackbot.hook;

import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.inject.Inject;

import spark.Request;
import spark.Response;
import spark.Route;

/** The route GitLabs sends the POST webhook to. */
public class HookRoute implements Route
{
	/** The name of the header encoding the gitlab event. */
	private static final String GITLAB_EVENT_HEADER = "X-Gitlab-Event";

	/** Processor for pipeline webhooks. */
	@Inject
	private List<Hook<?>> hooks;

	/** GSON. */
	@Inject
	private Gson gson;

	/** {@inheritDoc} */
	@Override
	public Object handle(Request request, Response response) throws Exception
	{
		String event = request.headers(GITLAB_EVENT_HEADER);
		Optional<Hook<?>> hook = this.hooks.stream().filter(
				h -> h.eventName().equals(event)).findFirst();
		if (!hook.isPresent())
		{
			throw new Exception("Event handler not found for hook: " + event);
		}

		handle(request.body(), hook.get());

		return "success";
	}

	/** Parses the request data and delegates to the hook handler. */
	private <T> void handle(String body, Hook<T> hook)
	{
		T data = this.gson.fromJson(
				body,
				hook.eventClass());
		hook.handleEvent(data);
	}
}
