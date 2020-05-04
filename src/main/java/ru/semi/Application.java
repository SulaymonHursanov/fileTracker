package ru.semi;


import lombok.extern.slf4j.Slf4j;
import ru.semi.config.Config;
import ru.semi.fileTracker.FileTrackerService;
import ru.semi.services.EventService;
import ru.semi.slackApi.SlackApiService;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Application {
	public static void main(String[] args) throws IOException, InterruptedException {
		Config config = new Config();
		List<String> filePaths = config.readFromYaml("config/fileTracker.yml");
		SlackApiService slackApiService = new SlackApiService();
		EventService eventService = new EventService(slackApiService);
		FileTrackerService fileTracker = new FileTrackerService(eventService);
		log.info("following files: {}", filePaths);
		fileTracker.startFollowing(30, filePaths);
	}

}

