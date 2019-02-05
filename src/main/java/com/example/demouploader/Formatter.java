package com.example.demouploader;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Component
public class Formatter {

	public ZonedDateTime dateTime(long lastModified) {
		return Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault());
	}

	public String size(long length) {
		DataSize dataSize = DataSize.ofBytes(length);
		if (dataSize.toGigabytes() > 0) {
			return dataSize.toGigabytes() + "GB";
		}
		if (dataSize.toMegabytes() > 0) {
			return dataSize.toMegabytes() + "MB";
		}
		if (dataSize.toKilobytes() > 0) {
			return dataSize.toKilobytes() + "KB";
		}
		return dataSize.toString();
	}
}
