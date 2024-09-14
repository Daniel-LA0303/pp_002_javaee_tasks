package org.mx.project.management.controllers.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.models.Project;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.impl.ProjectServiceImpl;

import com.google.gson.Gson;

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
		Connection conn = null;
		System.out.println("invocado");

		try {
			conn = (Connection) req.getAttribute("conn");
			if (conn == null) {
				throw new ServletException("Database connection not found");
			}

			ProjectService projectService = new ProjectServiceImpl(conn);

			String title = req.getParameter("title");
			String description = req.getParameter("description");
			String startDateStr = req.getParameter("startDate");
			String endDateStr = req.getParameter("endDate");
			String userId = req.getParameter("userId");

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

			if (userId == null || userId.isBlank()) {
				resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorizated");
			}

			if (!errors.isEmpty()) {
				System.out.println("Hay errores");

				resp.setContentType("application/json");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write(new Gson().toJson(errors));
				return;
			}

			LocalDate startDate = LocalDate.parse(startDateStr);
			LocalDate endDate = LocalDate.parse(endDateStr);

			Project project = new Project();
			project.setTitle(title);
			project.setDescription(description);
			project.setStartDate(startDate);
			project.setEndDate(endDate);
			project.setUserId(Long.parseLong(userId));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
