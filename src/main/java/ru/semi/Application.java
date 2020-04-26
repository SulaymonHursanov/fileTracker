package ru.semi;


import ru.semi.services.EventService;
import ru.semi.fileTracker.FileTrackerService;
import ru.semi.slackApi.SlackApiService;

import java.io.IOException;

public class Application {
	public static void main(String[] args) throws IOException, InterruptedException {
		SlackApiService slackApiService = new SlackApiService();
		EventService eventService = new EventService(slackApiService);
		FileTrackerService fileTracker = new FileTrackerService(eventService);
		fileTracker.startFollowing(30, args[0]);
	}


}

