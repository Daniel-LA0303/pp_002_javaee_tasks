package org.mx.project.management.controllers;

import java.io.IOException;
import java.sql.Connection;

import org.mx.project.management.models.User;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
		// TODO Auto-generated method stub
		getServletContext().getRequestDispatcher("/register.jsp").forward(req, resp);

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
		UserService userService = new UserServiceImpl(conn);

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");

		User user = new User();

		user.setEmail(email);
		user.setName(username);
		user.setPassword(password);

		userService.saveUser(user);

		HttpSession session = req.getSession();
		session.setAttribute("email", email);

		Cookie emailCookie = new Cookie("email", email);
		emailCookie.setMaxAge(60 * 60); // La cookie expirará en una hora
		emailCookie.setPath("/"); // Asegúrate de que el path sea adecuado para que la cookie esté disponible en
									// toda la aplicación
		resp.addCookie(emailCookie);

		resp.setContentType("application/json");
		// resp.getWriter().write("{\"message\":\"Registro exitoso!\"}");
		resp.sendRedirect(req.getContextPath() + "/principal.html");

	}

}
