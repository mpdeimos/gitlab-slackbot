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
}