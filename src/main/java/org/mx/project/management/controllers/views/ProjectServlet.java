package org.mx.project.management.controllers.views;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/project", "/project.html" })
public class ProjectServlet extends HttpServlet{
	
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
		
		String projectId = req.getParameter("id");
		 
		req.setAttribute("projectId", projectId);
		
		getServletContext().getRequestDispatcher("/project.jsp").forward(req, resp);
	}

}
