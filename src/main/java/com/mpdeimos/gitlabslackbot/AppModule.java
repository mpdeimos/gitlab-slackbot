package com.mpdeimos.gitlabslackbot;

import org.aeonbits.owner.ConfigFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

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

	/** {@inheritDoc} */
	@Override
	protected void configure()
	{
		// annotation based configuration
	}

}
