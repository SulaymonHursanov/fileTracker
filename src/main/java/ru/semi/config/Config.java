package ru.semi.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
	public List<String> readFromYaml (String path) throws IOException {
		Stream<String> lines = Files.lines(Paths.get(path), StandardCharsets.UTF_8);
		Map<String, List<String>> properties = new HashMap<>();
		String property = null;
		for (String line : lines.collect(Collectors.toList())) {
			if (!(line.trim()).startsWith("-")) {
				property = line.replace(":", "");
				properties.put(property, new ArrayList<>());
			} else if (properties.containsKey(property) && line.trim().startsWith("-")) {
				List<String> propsValues = properties.get(property);
				propsValues.add(line.substring(line.indexOf("-") + 1).trim());
			}
		}
		Optional<List<String>> first = properties.values().stream().findFirst();
		return first.orElseThrow(() -> new IllegalArgumentException("Not found filePaths props, check for properties file: " + path));
	}
}
