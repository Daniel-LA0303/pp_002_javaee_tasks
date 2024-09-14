package org.mx.project.management.controllers.tasks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.models.Task;
import org.mx.project.management.services.TaskService;
import org.mx.project.management.services.impl.TaskServiceImpl;

import com.google.gson.Gson;

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

}
