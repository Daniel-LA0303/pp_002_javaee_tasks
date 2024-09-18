package org.mx.project.management.controllers.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.models.Project;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.impl.ProjectServiceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/projects" })
public class ProjectsServelt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServlet#doDelete(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		Long projectId = jsonObject.get("projectId").getAsLong();

		System.out.println(userId);

		if (userId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to post\"}");
			return;
		}

		try {
			Project project = projectService.findProjectById(projectId);

			if (userId != project.getUserId()) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to delete this project\"}");
				return;
			} else {
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				resp.setStatus(HttpServletResponse.SC_OK);

				Gson gson = new Gson();
				String message = projectService.deleteProject(project.getUserId());
				String json = gson.toJson(message);
				resp.getWriter().write(json);
			}

		} catch (Exception e) {

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServlet#doGet(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		System.out.println("invocado");

		try {
			conn = (Connection) req.getAttribute("conn");
			if (conn == null) {
				throw new ServletException("Database connection not found");
			}

			ProjectService projectService = new ProjectServiceImpl(conn);
			List<Project> projects = projectService.findAllProjects();

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(projects);
			resp.getWriter().write(json);

		} catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Database error occurred\"}");
			e.printStackTrace();

		} catch (Exception e) {

			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"An unexpected error occurred\"}");
			e.printStackTrace();

		} finally {

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServlet#doPost(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		String title = jsonObject.get("title").getAsString();
		String description = jsonObject.get("description").getAsString();
		String startDateStr = jsonObject.get("startDate").getAsString();
		String endDateStr = jsonObject.get("endDate").getAsString();
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;

		System.out.println("Received Parameters: " + title + ", " + description + ", " + startDateStr + ", "
				+ endDateStr + ", " + userId);
		Map<String, String> errors = new HashMap<>();

		if (title == null || title.isBlank()) {
			errors.put("title", "Title is required");
		}

		if (description == null || description.isBlank()) {
			errors.put("description", "Description is required");
		}

		if (startDateStr == null || startDateStr.isBlank()) {
			errors.put("startDate", "Start Date is required");
		}

		if (endDateStr == null || endDateStr.isBlank()) {
			errors.put("endDate", "End Date is required");
		}

		if (userId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to post\"}");
			return;
		}

		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		Project project = new Project();
		project.setTitle(title);
		project.setDescription(description);
		project.setStartDate(LocalDate.parse(startDateStr));
		project.setEndDate(LocalDate.parse(endDateStr));
		project.setUserId(userId);

		try {
			projectService.saveProject(project);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		resp.setContentType("application/json");
		resp.setStatus(HttpServletResponse.SC_CREATED);
		resp.getWriter().write("{\"message\":\"Project created successfully\"}");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.http.HttpServlet#doPut(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		String title = jsonObject.get("title").getAsString();
		String description = jsonObject.get("description").getAsString();
		String startDateStr = jsonObject.get("startDate").getAsString();
		String endDateStr = jsonObject.get("endDate").getAsString();
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;

		Boolean status = jsonObject.has("status") ? jsonObject.get("status").getAsBoolean() : null;
		Long projectId = jsonObject.get("projectId").getAsLong();

		// quitar
		System.out.println("Title: " + title);
		System.out.println("Description: " + description);
		System.out.println("Start Date: " + startDateStr);
		System.out.println("End Date: " + endDateStr);
		System.out.println("User ID: " + userId);
		System.out.println("Status: " + status);
		System.out.println("Project ID: " + projectId);

		// Manejo de errores
		Map<String, String> errors = new HashMap<>();

		if (title == null || title.isBlank()) {
			errors.put("title", "Title is required");
		}

		if (description == null || description.isBlank()) {
			errors.put("description", "Description is required");
		}

		if (startDateStr == null || startDateStr.isBlank()) {
			errors.put("startDate", "Start Date is required");
		}

		if (endDateStr == null || endDateStr.isBlank()) {
			errors.put("endDate", "End Date is required");
		}

		if (status == null) {
			errors.put("status", "Staus is required");
		}

		if (userId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to update this project\"}");
			return;
		}

		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		try {
			Project p = projectService.findProjectById(projectId);

			if (p == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project not found\"}");
				return;
			}

			if (!userId.equals(p.getUserId())) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to update this project\"}");
				return;
			}

			System.out.println("Project found: " + p.getTitle());
			Project project = new Project();
			project.setId(projectId);
			project.setTitle(title);
			project.setDescription(description);
			project.setStartDate(LocalDate.parse(startDateStr));
			project.setEndDate(LocalDate.parse(endDateStr));
			project.setUserId(userId);
			project.setStatus(status);

			projectService.updateProject(project);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Project updated successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"An error occurred while updating the project\"}");
		}

	}

}
