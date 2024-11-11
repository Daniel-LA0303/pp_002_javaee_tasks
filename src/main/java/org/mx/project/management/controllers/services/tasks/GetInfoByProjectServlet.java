package org.mx.project.management.controllers.services.tasks;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;
import org.mx.project.management.models.Task;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.TaskService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.TaskServiceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/get-info-by-project" })
public class GetInfoByProjectServlet extends HttpServlet {

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
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);

		String projectIdStr = req.getParameter("projectId");
				
		Long projectId = 0L;
		projectId = Long.parseLong(projectIdStr);
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		try {
			// get project
			Project project = projectService.findProjectById(projectId);
			if (project == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project not found\"}");
				return;
			}

			// get all tasks by project
			List<Task> tasks = taskService.findAllTasksBYProjectId(projectId);
			
			List<UserByProjectDTO> usersAigned = projectService.getUsersAsignedToProject(projectId);
			
			Map<String, Object> responseMap = new HashMap<>();
		    responseMap.put("project", project); 
		    responseMap.put("tasks", tasks);
		    responseMap.put("users", usersAigned);

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(responseMap);
			resp.getWriter().write(json);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		Long taskId = jsonObject.has("taskId") ? jsonObject.get("taskId").getAsLong() : null;
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		
		System.out.println("task id " + taskId);
		System.out.println("user id " + userId);
		
		try {
	        Task task = taskService.findTaskById(taskId);
	        
	        if (task == null) {
	            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            resp.getWriter().write("{\"message\":\"Task not found\"}");
	            return;
	        }
	        
	        if (!task.getUserAsignedId().equals(userId)) {
	            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            resp.getWriter().write("{\"message\":\"You are not authorized to complete this task\"}");
	            return;
	        }
	        
	        taskService.setTaskStatusToTrue(taskId);
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"Task completed successfully\"}");
	        
	    } catch (Exception e) {
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while completing the task.\"}");
	        e.printStackTrace();
	    }
	}
	
	
	
	

}
