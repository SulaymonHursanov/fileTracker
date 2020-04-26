package ru.semi.fileTracker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.semi.dto.LogDto;
import ru.semi.dto.Message;
import ru.semi.services.EventService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
public class FileTrackerService {

	private final EventService eventService;

	public FileTrackerService(EventService eventService) {
		this.eventService = eventService;
	}

	public void startFollowing (int frequencySeconds, String filePath) throws IOException, InterruptedException {
		int position = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Message> logMapCount = new HashMap<>();
//		Map<String, Integer> filePosition = new HashMap<>();
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("linux")) {
			while (true) {
				log.info("checking for changes");
				int lastLine = getLastLineNumber(filePath);
				if (position < lastLine) {
					List<String> linesList = readFromLineNumber(position, lastLine, filePath);
					position = lastLine;
					countDuplicates(objectMapper, logMapCount, linesList);

				}
				// collecting messages after 10 min passed of message
				List<Message> messages = logMapCount.values().stream()
						.filter(message -> message.getFirstUpdate().isAfter(LocalDateTime.now().minusMinutes(1)))
						.collect(Collectors.toList());

				// removing messages which will be sent
				messages.forEach(message -> logMapCount.remove(message.getText()));

				// post messages from here
				if (messages.size() <= 50 && messages.size() > 0) {
					eventService.sendMessages(messages);
				} else if (messages.size() > 50){
					eventService.sendWarning("Warning! There is multiple fails of the services. (here should be traceId's)");
				}
				Thread.sleep(frequencySeconds * 1_000);
			}
		}else {
			throw new UnsupportedOperationException("Not implemented yet for " + osName);
		}
	}

	private void countDuplicates(ObjectMapper objectMapper, Map<String, Message> logMapCount, List<String> linesList) throws com.fasterxml.jackson.core.JsonProcessingException {
		String collect = linesList.stream().collect(Collectors.joining(",", "[", "]"));
		List<LogDto> logDtos = objectMapper.readValue(collect, new TypeReference<List<LogDto>>() {});
		log.info("exception log count: {}", logDtos.size());
		logDtos.forEach(logDto -> {
			String stackTrace;
			if (isNull(logDto.getStackTrace())) {
				stackTrace = logDto.getMessage();
			}else {
				stackTrace = logDto.getStackTrace().substring(0, logDto.getStackTrace().indexOf("\n"));
			}
			if (nonNull(stackTrace)) {
				Message message;
				if (logMapCount.containsKey(stackTrace)) {
					message = logMapCount.get(stackTrace);
					message.setLastUpdate(LocalDateTime.now());
					message.setDuplicateCount(message.getDuplicateCount() + 1);
				} else {
					message = new Message(stackTrace);
				}
				logMapCount.put(stackTrace, message);
			}
		});
	}

	public List<String> readFromLineNumber (int from, int to, String filePath) throws IOException {
		List<String> linesList;
		try(Stream<String> lines = 	Files.lines(Paths.get(filePath))) {
			linesList = lines.skip(from).limit(to)
					.map(String::trim)
					.filter(line -> !line.isEmpty())
					.collect(Collectors.toList());
		}
		return linesList;
	}

	public int getLastLineNumber(String filePath) {
		String[] command = new String[] {"wc", "-l", filePath};
		ProcessBuilder pb = new ProcessBuilder(command);
		try {
			InputStream inputStream = pb.start().getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			String[] splitByWords = line.split(" ");
			if (splitByWords.length > 0) {
				return Integer.parseInt(splitByWords[0]);
			}
			throw new IllegalArgumentException("something went wrong, please check source");
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
