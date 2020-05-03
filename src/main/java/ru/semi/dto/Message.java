package ru.semi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	public Message(String text,String fileName) {
		this.text = text;
		this.fileName = fileName;
		this.setDuplicateCount(1);
		this.setFirstUpdate(LocalDateTime.now());
		this.setLastUpdate(LocalDateTime.now());
	}

	private String text;
	private String fileName;
	private Integer duplicateCount;
	private LocalDateTime lastUpdate;
	private LocalDateTime firstUpdate;
}
