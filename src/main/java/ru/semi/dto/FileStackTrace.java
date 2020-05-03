package ru.semi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public final class FileStackTrace {
	private String filePath;
	private String stackTrace;
}
