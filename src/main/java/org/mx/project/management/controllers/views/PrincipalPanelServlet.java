package org.mx.project.management.controllers.views;

import java.io.IOException;
import java.sql.Connection;

import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/principal", "/principal.html" })
public class PrincipalPanelServlet extends HttpServlet {

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
		
		
		
		
		getServletContext().getRequestDispatcher("/principalPanel.jsp").forward(req, resp);
	}

}
