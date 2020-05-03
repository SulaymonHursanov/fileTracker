package ru.semi.slackApi;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SlackApiService {

	private final String token;
	private final Slack slack;

	public SlackApiService() {
		this.token = System.getenv("SLACK_API_BOT_TOKEN");
		requireNonNull(token, "Environment SLACK_API_BOT_TOKEN is empty or null");
		this.slack = Slack.getInstance();
	}

	public void postMessage (String channel, String text) throws IOException, SlackApiException {
		slack.methods(token).chatPostMessage(req -> req
				.channel(channel)
				.text(text));
	}
}
