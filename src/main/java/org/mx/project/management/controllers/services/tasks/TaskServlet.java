package org.mx.project.management.controllers.services.tasks;

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

@WebServlet({ "/tasks" })
public class TaskServlet extends HttpServlet {

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
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		Long projectId = jsonObject.get("projectId").getAsLong();
		Long userProjectId = jsonObject.has("userProjectId") ? jsonObject.get("userProjectId").getAsLong() : null;
		Long taskToUpdateId = jsonObject.has("taskToUpdateId") ? jsonObject.get("taskToUpdateId").getAsLong() : null;

		if (userProjectId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to update this project\"}");
			return;
		}

		try {

			Project p = projectService.findProjectById(projectId);
			Task task = taskService.findTaskById(taskToUpdateId);

			if (p == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project do not exists for this task\"}");
				return;
			}

			if (task == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Task do not exists\"}");
				return;
			}

			if (userProjectId != p.getUserId()) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to delete this task\"}");
				return;
			}

			taskService.deleteTask(task.getId());

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Task deleted successfully\"}");

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"An error occurred while updating the project\"}");
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

			TaskService taskService = new TaskServiceImpl(conn);
			List<Task> tasks = taskService.findAllTasks();

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(tasks);
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
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		String title = jsonObject.get("title").getAsString();
		String description = jsonObject.get("description").getAsString();
		String dueDate = jsonObject.get("dueDate").getAsString();
		String priority = jsonObject.get("priority").getAsString();
		Long userAsignedId = jsonObject.has("userAsignedId") ? jsonObject.get("userAsignedId").getAsLong() : null;
		Long projectId = jsonObject.get("projectId").getAsLong();
		Long userProjectId = jsonObject.has("userProjectId") ? jsonObject.get("userProjectId").getAsLong() : null;

		System.out.println("***********se ejecuto post ");
		System.out.println("Title: " + title);
		System.out.println("Description: " + description);
		System.out.println("Due Date: " + dueDate);
		System.out.println("Priority: " + priority);
		System.out.println("User Assigned ID: " + userAsignedId);
		System.out.println("Project ID: " + projectId);
		System.out.println("User Project ID: " + userProjectId);

		
		// Manejo de errores
		Map<String, String> errors = new HashMap<>();

		if (title == null || title.trim().isEmpty()) {
		    errors.put("title", "Title is required");
		}

		if (description == null || description.trim().isEmpty()) {
		    errors.put("description", "Description is required");
		}

		if (dueDate == null || dueDate.trim().isEmpty()) {
		    errors.put("dueDate", "Due date is required");
		}

		if (priority == null || priority.trim().isEmpty()) {
		    errors.put("priority", "Priority is required");
		}

		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		try {

			Project p = projectService.findProjectById(projectId);
			User userAsigned = userService.findUserBYId(userAsignedId);

			if (p == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project do not exists\"}");
				return;
			}

			if (userAsigned == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User asigned to task do not exists\"}");
				return;
			}

			if (userProjectId != p.getUserId()) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to create tasks in this project\"}");
				return;
			}

			Task newTask = new Task();
			newTask.setTitle(title);
			newTask.setDescription(description);
			newTask.setDueDate(LocalDate.parse(dueDate));
			newTask.setPriority(priority);
			newTask.setUserAsignedId(userAsigned.getId());
			newTask.setProjectId(p.getId());

			taskService.saveTask(newTask);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Task created successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"An error occurred while updating the project\"}");
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
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);

		String title = jsonObject.get("title").getAsString();
		String description = jsonObject.get("description").getAsString();
		String dueDate = jsonObject.get("dueDate").getAsString();
		String priority = jsonObject.get("priority").getAsString();
		//Boolean status = jsonObject.has("status") ? jsonObject.get("status").getAsBoolean() : null;

		Long userAsignedId = jsonObject.has("userAsignedId") ? jsonObject.get("userAsignedId").getAsLong() : null;
		Long projectId = jsonObject.get("projectId").getAsLong();
		Long userProjectId = jsonObject.has("userProjectId") ? jsonObject.get("userProjectId").getAsLong() : null;
		Long taskToUpdateId = jsonObject.has("taskToUpdateId") ? jsonObject.get("taskToUpdateId").getAsLong() : null;
		
		System.out.println("**************se ejecuto PUT");
		System.out.println("Title: " + title);
		System.out.println("Description: " + description);
		System.out.println("Due Date: " + dueDate);
		System.out.println("Priority: " + priority);
		System.out.println("User Asigned ID: " + (userAsignedId != null ? userAsignedId : "Not provided"));
		System.out.println("Project ID: " + projectId);
		System.out.println("User Project ID: " + (userProjectId != null ? userProjectId : "Not provided"));
		System.out.println("Task to update ID: " + (taskToUpdateId != null ? taskToUpdateId : "Not provided"));
		//System.out.println("status: " + (status != null ? status : "Not provided"));

		if (userProjectId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to update this project\"}");
			return;
		}

		Map<String, String> errors = new HashMap<>();

		if (title == null || title.trim().isEmpty()) {
		    errors.put("title", "Title is required");
		}

		if (description == null || description.trim().isEmpty()) {
		    errors.put("description", "Description is required");
		}

		if (dueDate == null || dueDate.trim().isEmpty()) {
		    errors.put("dueDate", "Due date is required");
		}

		if (priority == null || priority.trim().isEmpty()) {
		    errors.put("priority", "Priority is required");
		}


		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		try {

			Project p = projectService.findProjectById(projectId);
			User userAsigned = userService.findUserBYId(userAsignedId);
			Task taskToUpdate = taskService.findTaskById(taskToUpdateId);

			if (p == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project do not exists\"}");
				return;
			}

			if (taskToUpdate == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Task do not exists\"}");
				return;
			}

			if (userAsigned == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User asigned to task do not exists\"}");
				return;
			}

			if (userProjectId != p.getUserId()) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"You are not authorized to create tasks in this project\"}");
				return;
			}

			Task updateTask = new Task();
			updateTask.setId(taskToUpdate.getId());
			updateTask.setTitle(title);
			updateTask.setDescription(description);
			updateTask.setDueDate(LocalDate.parse(dueDate));
			updateTask.setPriority(priority);
			updateTask.setUserAsignedId(userAsigned.getId());
			updateTask.setStatus(taskToUpdate.getStatus());
			updateTask.setProjectId(p.getId());

			
			taskService.updateTask(updateTask);

			
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Task updated successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"An error occurred while updating the project\"}");
		}
	}

}
