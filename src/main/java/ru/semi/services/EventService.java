package ru.semi.services;

import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import ru.semi.dto.Message;
import ru.semi.slackApi.SlackApiService;

import java.io.IOException;
import java.util.List;

@Slf4j
public class EventService {

	private final SlackApiService slackApiService;
	private final String channel = "test";

	public EventService (SlackApiService slackApiService) {
		this.slackApiService = slackApiService;
	}

	public void sendMessages(List<Message> messages) {
		log.info("sending messages");
		try {
			for (Message message : messages) {
				String text = message.getFileName() + " " + message.getText() + " count: " + message.getDuplicateCount();
				slackApiService.postMessage(channel, text);
			}
			log.info("messages are sent successfully");
		} catch (SlackApiException | IOException e) {
			log.error("Could not send all messages to slack");
			e.printStackTrace();
		}
	}

	public void sendWarning(String warningMessage) {
		log.info("sending warning message");
		try {
			slackApiService.postMessage(channel, warningMessage);
		} catch (IOException | SlackApiException e) {
			log.error("Could not send warning message to slack");
			e.printStackTrace();
		}
	}
}
