package org.mx.project.management.config.jsonConverter;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;

/**
 * to convert json
 */
public class JsonUtils {

	public static JsonObject parseJsonRequest(HttpServletRequest request) throws IOException {
		StringBuilder jsonBuilder = new StringBuilder();

		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuilder.append(line);
			}
		}

		String jsonString = jsonBuilder.toString();
		Gson gson = new Gson();
		return gson.fromJson(jsonString, JsonObject.class);
	}
}