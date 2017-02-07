package com.mpdeimos.gitlabslackbot;

import com.mpdeimos.gitlabslackbot.hook.HookRoute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import spark.Service;

/** Main application. */
public class App
{
	/** Logger. */
	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	/** The application server. */
	@Inject
	private Service spark;

	/** The GitLab webhook route. */
	@Inject
	private HookRoute hookRoute;

	/** Serves the application */
	public void serve()
	{
		this.spark.get(
				"/",
				(request, response) -> "GitLab Slack Bot is running.");

		this.spark.post("/hook", this.hookRoute);

		this.spark.exception(
				Exception.class,
				(exception, request, response) -> {
					LOGGER.error(
							"Error handling request: " + request.pathInfo(),
							exception);

					response.status(500);
					response.body(exception.getMessage());
				});
	}

	/** Application entry point. */
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new AppModule());
		injector.getInstance(App.class).serve();
	}
}
