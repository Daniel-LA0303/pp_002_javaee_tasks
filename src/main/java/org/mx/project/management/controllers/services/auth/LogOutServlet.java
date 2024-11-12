package org.mx.project.management.controllers.services.auth;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/logout", "/logout.html" })
public class LogOutServlet extends HttpServlet {

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
		req.getSession().invalidate();

		// delete cookie
		Cookie emailCookie = new Cookie("email", null);
		emailCookie.setMaxAge(0); // Expired
		emailCookie.setPath("/"); 
		
		Cookie userIdCookie = new Cookie("userId", null);
		userIdCookie.setMaxAge(0);
		userIdCookie.setPath("/");
		
		resp.addCookie(emailCookie);
		resp.addCookie(userIdCookie);

		resp.sendRedirect(req.getContextPath() + "/login.html");
	}

}
