package ru.semi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trace {
	private String service;
	@JsonProperty("trace_id")
	private String traceId;
	@JsonProperty("span_Id")
	private String spanId;
}
