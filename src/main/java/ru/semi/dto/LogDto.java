package ru.semi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogDto {
	@JsonProperty("stack_trace")
	private String stackTrace;
	private Trace trace;
	private String message;
}
