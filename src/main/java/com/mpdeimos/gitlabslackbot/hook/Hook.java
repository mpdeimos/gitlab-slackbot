package com.mpdeimos.gitlabslackbot.hook;

/**
 * Describes a hook processor for handling an event with a given event class.
 */
public interface Hook<T>
{
	/** The name of the event. */
	public String eventName();

	/** The class of the event payload. */
	public Class<T> eventClass();

	/** Handles the event. */
	public void handleEvent(T event) throws Exception;
}
