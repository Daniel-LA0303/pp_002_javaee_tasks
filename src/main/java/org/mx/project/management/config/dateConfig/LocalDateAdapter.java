package org.mx.project.management.config.dateConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * config to read dates
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {
	private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	public LocalDate read(JsonReader in) throws IOException {
		return LocalDate.parse(in.nextString(), formatter);
	}

	@Override
	public void write(JsonWriter out, LocalDate value) throws IOException {
		out.value(value.format(formatter));
	}
}
