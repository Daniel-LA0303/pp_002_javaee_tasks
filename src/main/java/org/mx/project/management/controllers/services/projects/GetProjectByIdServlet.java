package org.mx.project.management.controllers.services.projects;

import java.io.IOException;
import java.sql.Connection;

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

@WebServlet({ "/project-by-id" })
public class GetProjectByIdServlet extends HttpServlet {

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
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);

		System.out.println("invocado");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		// Long userId = jsonObject.get("userId").getAsLong();
		Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong() : null;

		System.out.println("projectId" + projectId);

		try {
			Project project = projectService.findProjectById(projectId);

			if (project == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project not found\"}");
				return;
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(project);
			resp.getWriter().write(json);
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Database error occurred\"}");
			e.printStackTrace();
		}
	}

}
