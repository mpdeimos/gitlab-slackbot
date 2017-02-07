package com.mpdeimos.gitlabslackbot;

import com.mpdeimos.gitlabslackbot.hook.Hook;
import com.mpdeimos.gitlabslackbot.hook.PipelineHook;

import java.util.Arrays;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.gpedro.integrations.slack.SlackApi;
import spark.Service;

/** Guice default application module. */
public class AppModule extends AbstractModule
{
	/** Provides the application configuration. */
	@Provides
	@Singleton
	public AppConfig provideAppConfig()
	{
		return ConfigFactory.create(AppConfig.class, System.getenv());
	}

	/** Provides the spark service. */
	@Provides
	@Singleton
	public Service provideService(AppConfig config)
	{
		return Service.ignite().port(config.serverPort());
	}

	/** Provides the Slack API. */
	@Provides
	@Singleton
	public SlackApi provideSlackApi(AppConfig config)
	{
		return new SlackApi(config.slackWebhook());
	}

	/** Provides a list of all hook processors. */
	@Provides
	public List<Hook<?>> provideHookProcessor(PipelineHook pipelineHook)
	{
		return Arrays.asList(pipelineHook);
	}

	/** Provides GSON. */
	@Provides
	@Singleton
	public Gson provideGson()
	{
		return new GsonBuilder().setPrettyPrinting().create();
	}

	/** {@inheritDoc} */
	@Override
	protected void configure()
	{
		// annotation based configuration
	}

}
