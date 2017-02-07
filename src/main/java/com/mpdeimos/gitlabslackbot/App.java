package com.mpdeimos.gitlabslackbot;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import spark.Service;

/** Main application. */
public class App
{
	/** The application server. */
	@Inject
	private Service spark;

	/** Serves the application */
	public void serve()
	{
		this.spark.get(
				"/",
				(request, response) -> "GitLab Slack Bot is running.");
	}

	/** Application entry point. */
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new AppModule());
		injector.getInstance(App.class).serve();
	}
}
