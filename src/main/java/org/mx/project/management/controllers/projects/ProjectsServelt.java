package org.mx.project.management.controllers.projects;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

			// Configurar el tipo de contenido de la respuesta
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			// Convertir la lista de proyectos a JSON y escribir en la respuesta
			Gson gson = GsonConfig.createGson(); // Usa el Gson configurado
			String json = gson.toJson(projects);
			resp.getWriter().write(json);

		} catch (SQLException e) {
			// Manejo de excepciones SQL
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Database error occurred\"}");
			e.printStackTrace(); // O usar un logger para registrar la excepción

		} catch (Exception e) {
			// Manejo de otras excepciones
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"An unexpected error occurred\"}");
			e.printStackTrace(); // O usar un logger para registrar la excepción

		} finally {
			// Cierre de recursos si es necesario
			// if (conn != null) conn.close(); // Solo si manejas conexiones directamente
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
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
