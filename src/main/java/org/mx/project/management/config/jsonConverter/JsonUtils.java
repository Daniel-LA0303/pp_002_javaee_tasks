package org.mx.project.management.config.jsonConverter;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;

public class JsonUtils {

	public static JsonObject parseJsonRequest(HttpServletRequest request) throws IOException {
		StringBuilder jsonBuilder = new StringBuilder();

		// Leer el contenido del request y construir el JSON
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBuilder.append(line);
			}
		}

		String jsonString = jsonBuilder.toString();

		// Convertir el JSON en JsonObject usando Gson
		Gson gson = new Gson();
		return gson.fromJson(jsonString, JsonObject.class);
	}
}