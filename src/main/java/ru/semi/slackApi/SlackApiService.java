package ru.semi.slackApi;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;

import java.io.IOException;

public class SlackApiService {

	private final String token;
	private final Slack slack;

	public SlackApiService() {
		this.token = System.getenv("SLACK_API_BOT_TOKEN");
		this.slack = Slack.getInstance();
	}

	public void postMessage (String channel, String text) throws IOException, SlackApiException {
		slack.methods(token).chatPostMessage(req -> req
				.channel(channel)
				.text(text));
	}
}
