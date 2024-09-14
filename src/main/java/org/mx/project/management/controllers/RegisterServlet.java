package org.mx.project.management.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.mx.project.management.models.User;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.UserServiceImpl;

import com.google.gson.Gson;

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

		// Manejo de errores
		Map<String, String> errors = new HashMap<>();

		if (username == null || username.isBlank()) {
			errors.put("username", "Name is required");
		}

		if (password == null || password.isBlank()) {
			errors.put("password", "Password is required");
		}

		if (email == null || email.isBlank()) {
			errors.put("email", "Email is required");
		}

		if (!errors.isEmpty()) {
			System.out.println("Hay errores");

			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		Optional<User> userO = userService.findUserByEmail(email);
		if (userO.isPresent()) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT);
			resp.getWriter().write("{\"message\":\"User already exists\"}");
		} else {
			String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setPassword(encryptedPassword);
			userService.saveUser(user);

			HttpSession session = req.getSession();
			session.setAttribute("email", email);

			Cookie emailCookie = new Cookie("email", email);
			emailCookie.setMaxAge(60 * 60);
			emailCookie.setPath("/");
			resp.addCookie(emailCookie);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write("{\"message\":\"Registration successful\"}");
		}
	}

}
