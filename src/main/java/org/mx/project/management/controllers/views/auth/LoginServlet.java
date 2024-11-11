package org.mx.project.management.controllers.views.auth;

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

		/**
		 * get parameters
		 */
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		
		/**
		 * validation
		 */
		Map<String, String> errors = new HashMap<>();

		if (password == null || password.trim().isEmpty()) {
		    errors.put("password", "Password is required");
		}

		if (email == null || email.trim().isEmpty()) {
		    errors.put("email", "Email is required");
		}

		/**
		 * return errors
		 */
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
				
				// this user or email does not exists
				// status 404
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.getWriter().write("{\"message\":\"User does not exists\"}");
				
				
			} else if (BCrypt.checkpw(password, userO.get().getPassword())) {
				HttpSession session = req.getSession();
				session.setAttribute("email", email);

				Cookie emailCookie = new Cookie("email", email);
				emailCookie.setMaxAge(60 * 60);
				emailCookie.setPath("/");
				resp.addCookie(emailCookie);
				
				Cookie userIdCookie = new Cookie("userId", userO.get().getId().toString());
				userIdCookie.setMaxAge(60 * 60);
				userIdCookie.setPath("/");
				resp.addCookie(userIdCookie);

				// status 200
				resp.setStatus(HttpServletResponse.SC_OK);
				resp.getWriter().write("{\"message\":\"Login successful\"}");
			} else {
				
				// status 401
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.getWriter().write("{\"message\":\"Incorrect password\"}");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
