package org.mx.project.management.controllers.services.tasks;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.dto.UserByProjectDTO;
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
		Connection conn = (Connection) req.getAttribute("conn");
		TaskService taskService = new TaskServiceImpl(conn);
		ProjectService projectService = new ProjectServiceImpl(conn);
		UserService userService = new UserServiceImpl(conn);

		String projectIdStr = req.getParameter("projectId");
		Long projectId = Long.parseLong(projectIdStr);
		
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			// get project
			Project project = projectService.findProjectById(projectId);
			if (project == null) {
				// status 404
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project not found\"}");
				return;
			}

			User user = userService.findUserBYId(project.getUserId());

			// get all tasks by project
			List<Task> tasks = taskService.findAllTasksBYProjectId(projectId);
			
			// get all users assigned to project
			List<UserByProjectDTO> usersAigned = projectService.getUsersAsignedToProject(projectId);
			
			Map<String, Object> responseMap = new HashMap<>();
		    responseMap.put("project", project); // response project info 
		    responseMap.put("tasks", tasks); // response tasks 
		    responseMap.put("users", usersAigned); // response users
		    responseMap.put("autor", user.getEmail());

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

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
		
		// validation
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
			// status 400
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		try {

			Project p = projectService.findProjectById(projectId);
			User userAsigned = userService.findUserBYId(userAsignedId);

			if (p == null) {
				// status 404 not found project
				
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project do not exists\"}");
				return;
			}

			if (userAsigned == null) {
				// status 404 not found user
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User asigned to task do not exists\"}");
				return;
			}

			if (userProjectId != p.getUserId()) {
				// status 401
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

			// status 201
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Task created successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			// status 500
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
		Long userAsignedId = jsonObject.has("userAsignedId") ? jsonObject.get("userAsignedId").getAsLong() : null;
		Long projectId = jsonObject.get("projectId").getAsLong();
		Long userProjectId = jsonObject.has("userProjectId") ? jsonObject.get("userProjectId").getAsLong() : null;
		Long taskToUpdateId = jsonObject.has("taskToUpdateId") ? jsonObject.get("taskToUpdateId").getAsLong() : null;
		
		if (userProjectId == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"You are not authorized to update this project\"}");
			return;
		}

		// validation
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
				// status 404 not found project
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Project do not exists\"}");
				return;
			}

			if (taskToUpdate == null) {
				// status 404 not found task
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"Task do not exists\"}");
				return;
			}

			if (userAsigned == null) {
				// status 404 not found user assigned
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User asigned to task do not exists\"}");
				return;
			}

			if (userProjectId != p.getUserId()) {
				// status 401
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

			// status 200
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"Task updated successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			// status 500
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("application/json");
			resp.getWriter().write("{\"message\":\"An error occurred while updating the project\"}");
		}
	}

}
