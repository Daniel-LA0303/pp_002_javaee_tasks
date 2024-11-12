package org.mx.project.management.controllers.services.tasks;

import java.io.IOException;
import java.sql.Connection;

import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.models.Task;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.TaskService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.TaskServiceImpl;

import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/tasks-actions" })
public class TasksActionsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		TaskService taskService = new TaskServiceImpl(conn);
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		Long taskId = jsonObject.has("taskId") ? jsonObject.get("taskId").getAsLong() : null;
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;

		try {
	        
			// find task
			Task task = taskService.findTaskById(taskId);
	        if (task == null) {
	        	// status 404
	            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            resp.getWriter().write("{\"message\":\"Task not found\"}");
	            return;
	        }
	        
	        if (!task.getUserAsignedId().equals(userId)) {
	        	//status 401
	            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            resp.getWriter().write("{\"message\":\"You are not authorized to complete this task\"}");
	            return;
	        }
	        
	        taskService.setTaskStatusToTrue(taskId);
	        // status 200
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"Task completed successfully\"}");
	        
	    } catch (Exception e) {
	    	// status 500
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while completing the task.\"}");
	        e.printStackTrace();
	    }
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong() : null;
		
		try {
			projectService.assignUserToProject(userId, projectId);
			// status 200
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"User assigned to project successfully.\"}");
	    }  catch (Exception e) {
	        e.printStackTrace();
	        // status 500
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An unexpected error occurred.\"}");
	    }
		
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
		ProjectService projectService = new ProjectServiceImpl(conn);
		TaskService taskService = new TaskServiceImpl(conn);
		
		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;
		Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong() : null;
		
		try {	
			projectService.removeUserFromProject(userId, projectId);
			taskService.removeUserFromProject(userId, projectId);
			// status 200
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write("{\"message\":\"User removed to project successfully.\"}");
	    }catch (Exception e) {
	        e.printStackTrace();
	        // status 500
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An unexpected error occurred.\"}");
	    }
	}
	
	

}
