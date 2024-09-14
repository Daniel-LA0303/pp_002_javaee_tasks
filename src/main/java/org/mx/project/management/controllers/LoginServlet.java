package org.mx.project.management.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

@WebServlet({ "/login", "/login.html" })
public class LoginServlet extends HttpServlet {

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
		getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
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

		String password = req.getParameter("password");
		String email = req.getParameter("email");

		User user = new User();
		user.setEmail(email);
		user.setName(email);

		Map<String, String> errors = new HashMap<>();

		if (password == null || password.isBlank()) {
			errors.put("password", "Password is required");
		}

		if (email == null || email.isBlank()) {
			errors.put("email", "Email is required");
		}

		if (!errors.isEmpty()) {
			resp.setContentType("application/json");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}

		Optional<User> userO;
		try {
			userO = userService.findUserByEmail(email);

			if (userO.isEmpty()) {
				System.out.println("no existe este mail");
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
				resp.getWriter().write("{\"message\":\"User does not exists\"}");
			} else if (BCrypt.checkpw(password, userO.get().getPassword())) {
				HttpSession session = req.getSession();
				session.setAttribute("email", email);

				Cookie emailCookie = new Cookie("email", email);
				emailCookie.setMaxAge(60 * 60);
				emailCookie.setPath("/");
				resp.addCookie(emailCookie);

				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().write("{\"message\":\"Login successful\"}");
			} else {
				System.out.println("Contraseña incorrecta");
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.getWriter().write("{\"message\":\"Incorrect password\"}");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
