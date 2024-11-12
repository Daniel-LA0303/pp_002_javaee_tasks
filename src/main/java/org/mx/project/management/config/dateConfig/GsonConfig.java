package org.mx.project.management.config.dateConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * json config to responses
 */
public class GsonConfig {
	public static Gson createGson() {
		return new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	}
}
