package ru.semi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
	public static void main(String[] args) throws IOException, InterruptedException {
		Application app = new Application();
		app.startFollowing(5,"text.txt");
	}

	public void startFollowing (int frequencySeconds, String filePath) throws IOException, InterruptedException {
		int position = 0;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("linux")) {
			while (true) {
				int lastLine = getLastLineNumber(filePath);
				if (position < lastLine) {
					List<String> linesList = readFromLineNumber(position, lastLine, filePath);
					position = lastLine;
					// post messages from here
					System.out.println(linesList);
				}
				Thread.sleep(frequencySeconds * 1_000);
			}
		}else {
			throw new UnsupportedOperationException("Not implemented yet for " + osName);
		}
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
