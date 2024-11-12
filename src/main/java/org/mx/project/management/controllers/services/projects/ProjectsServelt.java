package org.mx.project.management.controllers.services.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.models.Project;
import org.mx.project.management.models.User;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.UserServiceImpl;

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

		try {
			Project project = projectService.findProjectById(projectId);

			if(project == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"This project not found.\"}");
			}
			
			if (userId != project.getUserId()) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to delete this project\"}");
				return;
			} 
			
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.setStatus(HttpServletResponse.SC_OK);

			Gson gson = new Gson();
			String message = projectService.deleteProject(project.getId());
			String json = gson.toJson(message);
			resp.getWriter().write(json);
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Error in server.\"}");
			e.printStackTrace();
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
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);

		String email = req.getParameter("email");
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		try {

			Optional<User> user = userService.findUserByEmail(email);
			if (user.isEmpty()) {
				// status 404
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User not found\"}");
				return;
			}
			
			// projects by user
			List<Project> projects = projectService.findAllProjectsByUserId(user.get().getId());
			
			// projects assigned by user
			List<Project> projectsAsigned = projectService.getProjectsByUserAigned(user.get().getId());

			Map<String, Object> responseMap = new HashMap<>();
		    responseMap.put("projects", projects); 
		    responseMap.put("projectsAsigned", projectsAsigned);

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(responseMap);
			
			// status 200
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(json);

		} catch (Exception e) {
			// status 500
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Error in server.\"}");
			e.printStackTrace();
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

		// validation
		Map<String, String> errors = new HashMap<>();

		if (title == null || title.trim().isEmpty()) {
		    errors.put("title", "Title is required");
		}

		if (description == null || description.trim().isEmpty()) {
		    errors.put("description", "Description is required");
		}

		if (startDateStr == null || startDateStr.trim().isEmpty()) {
		    errors.put("startDate", "Start Date is required");
		}

		if (endDateStr == null || endDateStr.trim().isEmpty()) {
		    errors.put("endDate", "End Date is required");
		}

		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			// status 400
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
			resp.setContentType("application/json");
			//status 201
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.getWriter().write("{\"message\":\"Project created successfully\"}");
		} catch (SQLException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Error in server.\"}");
			e.printStackTrace();
		}

		
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
		Long projectId = jsonObject.get("projectId").getAsLong();


		// validation
		Map<String, String> errors = new HashMap<>();

		if (title == null || title.trim().isEmpty()) {
		    errors.put("title", "Title is required");
		}

		if (description == null || description.trim().isEmpty()) {
		    errors.put("description", "Description is required");
		}

		if (startDateStr == null || startDateStr.trim().isEmpty()) {
		    errors.put("startDate", "Start Date is required");
		}

		if (endDateStr == null || endDateStr.trim().isEmpty()) {
		    errors.put("endDate", "End Date is required");
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

			Project project = new Project();
			project.setId(p.getId());
			project.setTitle(title);
			project.setDescription(description);
			project.setStartDate(LocalDate.parse(startDateStr));
			project.setEndDate(LocalDate.parse(endDateStr));
			project.setUserId(userId);
			project.setStatus(p.getStatus());
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
