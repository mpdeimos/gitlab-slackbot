package com.mpdeimos.gitlabslackbot;

import com.mpdeimos.gitlabslackbot.config.Config;

import spark.Spark;

public class App {
	public static void main(String[] args) {
		Config config = new Config();
		Spark.port(config.PORT);
		Spark.get("/", (request, response) -> "Hello World");
	}
}
