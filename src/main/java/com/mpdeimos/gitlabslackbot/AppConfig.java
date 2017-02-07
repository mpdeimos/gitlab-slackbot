package com.mpdeimos.gitlabslackbot;

import org.aeonbits.owner.Config;

/** The application configuration. */
public interface AppConfig extends Config
{
	/** The port the server will run on. */
	@Key("PORT")
	@DefaultValue("8080")
	public int serverPort();

	/** The slack webook endpoint. */
	@Key("SLACK_WEBHOOK")
	public String slackWebhook();

	/** The GitLab token to verify a request (may be null). */
	@Key("GITLAB_TOKEN")
	public String gitlabToken();

	/** An optional regex filtering the branch name. */
	@Key("FILTER_BRANCH")
	public String filterBranch();

	/** An optional regex filtering the project name. */
	@Key("FILTER_PROJECT")
	public String filterProject();
}