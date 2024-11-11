package org.mx.project.management.controllers.services.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.models.Project;
import org.mx.project.management.models.Task;
import org.mx.project.management.models.User;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.TaskService;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.TaskServiceImpl;
import org.mx.project.management.services.impl.UserServiceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/projects-user" })
public class GetAllProjecstByUserIdServlet extends HttpServlet {

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
		UserService userService = new UserServiceImpl(conn);

		String email = req.getParameter("email");
		//System.out.println("invocado get projects");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		//System.out.println(email);
		
		String em = "email1@email.com";

		try {

			Optional<User> user = userService.findUserByEmail(email);
			if (user.isEmpty()) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User not found\"}");
				return;
			}
			
			//System.out.println(user.get().getId() + " " + user.get().getName());

			List<Project> projects = projectService.findAllProjectsByUserId(user.get().getId());
			
			List<Project> projectsAsigned = projectService.getProjectsByUserAigned(user.get().getId());

			Map<String, Object> responseMap = new HashMap<>();
		    responseMap.put("projects", projects); 
		    responseMap.put("projectsAsigned", projectsAsigned);
			
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(responseMap);
			resp.getWriter().write(json);

		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Database error occurred\"}");
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong() : null;
		
		try {
			
			projectService.assignUserToProject(userId, projectId);

	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"User assigned to project successfully.\"}");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while assigning the user to the project.\"}");
	    } catch (Exception e) {
	        e.printStackTrace();
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An unexpected error occurred.\"}");
	    }
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);
		TaskService taskService = new TaskServiceImpl(conn);
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong() : null;
		
		try {
			
			projectService.removeUserFromProject(userId, projectId);
			taskService.removeUserFromProject(userId, projectId);
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"User removed to project successfully.\"}");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while assigning the user to the project.\"}");
	    } catch (Exception e) {
	        e.printStackTrace();
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An unexpected error occurred.\"}");
	    }
	}
	
	

}
