package com.mpdeimos.gitlabslackbot.hook;

import com.mpdeimos.gitlabslackbot.AppConfig;
import com.mpdeimos.gitlabslackbot.hook.PipelineHook.EventData;
import com.mpdeimos.gitlabslackbot.hook.PipelineHook.EventData.EStatus;

import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

/** Handles a GitLab pipeline hook. */
public class PipelineHook implements Hook<EventData>
{
	/** The slack API. */
	@Inject
	SlackApi slack;

	/** The application config. */
	@Inject
	AppConfig config;

	/** {@inheritDoc} */
	@Override
	public String eventName()
	{
		return "Pipeline Hook";
	}

	/** {@inheritDoc} */
	@Override
	public Class<EventData> eventClass()
	{
		return EventData.class;
	}

	/** {@inheritDoc} */
	@Override
	public void handleEvent(EventData event) throws Exception
	{
		checkProject(event);
		checkBranch(event);

		if (event.attributes.status != EStatus.FAILED)
		{
			return;
		}

		SlackMessage message = new SlackMessage(
				"Pipeline failed for *" + event.project.name + "* on branch *"
						+ event.attributes.ref + "*");

		SlackField field = new SlackField();
		field.setTitle(event.commit.message);

		String value = "<" + event.project.url + "/pipelines/"
				+ event.attributes.id + "|Pipeline>";
		value += " \u2022 <" + event.commit.url + "|Commit> by "
				+ event.commit.author.name;
		field.setValue(value);

		SlackAttachment attachment = new SlackAttachment().setFallback(
				event.commit.message).addFields(
						field).setColor("danger");
		message.addAttachments(attachment);

		this.slack.call(message);
	}

	/** Verifies that only allowed projects are processed. */
	private void checkProject(EventData event) throws Exception
	{
		String filterProject = this.config.filterProject();
		if (filterProject != null
				&& !Pattern.matches(filterProject, event.project.id))
		{
			throw new Exception(
					"Project " + event.project.id + " does not match "
							+ filterProject);
		}

	}

	/** Verifies that only allowed branches are processed. */
	private void checkBranch(EventData event) throws Exception
	{
		String filterBranch = this.config.filterBranch();
		if (filterBranch != null
				&& !Pattern.matches(filterBranch, event.attributes.ref))
		{
			throw new Exception(
					"Branch " + event.attributes.ref + " does not match "
							+ filterBranch);
		}

	}

	/** The data of this event. */
	public static class EventData
	{
		@SerializedName("object_attributes")
		public PipelineAttributes attributes;

		public Commit commit;

		public Project project;

		public static class PipelineAttributes
		{
			public int id;

			public String ref;

			public EStatus status;

		}

		public static enum EStatus
		{
			@SerializedName("pending")
			PENDING,
			@SerializedName("running")
			RUNNING,
			@SerializedName("success")
			SUCCESS,
			@SerializedName("failed")
			FAILED,
			@SerializedName("canceled")
			CANCELED
		}

		public static class Commit
		{
			public String message;

			public String url;

			public Author author;
		}

		public static class Project
		{
			@SerializedName("web_url")
			public String url;

			@SerializedName("path_with_namespace")
			public String id;

			public String name;
		}

		public static class Author
		{
			public String name;
		}
	}

}
