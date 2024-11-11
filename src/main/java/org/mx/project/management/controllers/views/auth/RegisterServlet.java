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
		// return view
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

		/**
		 * get parameters
		 */
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");


		/**
		 * validation 
		 */
		Map<String, String> errors = new HashMap<>();

		if (username == null || username.trim().isEmpty()) {
		    errors.put("username", "Name is required");
		}

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
			// status 400
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(new Gson().toJson(errors));
			return;
		}
		
		
		User user = new User();
		user.setEmail(email);
		user.setName(username);

		Optional<User> userO;
		try {
			
			/**
			 * get a user, if exists then user name can not register
			 */
			userO = userService.findUserByEmail(email);
			if (userO.isPresent()) {
				// status 409
				resp.setStatus(HttpServletResponse.SC_CONFLICT);
				resp.getWriter().write("{\"message\":\"User already exists\"}");
			} else {
				
				/**
				 * if does not exists user name then registered
				 */
				String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
				user.setPassword(encryptedPassword);
				userService.saveUser(user);

				HttpSession session = req.getSession();
				session.setAttribute("email", email);
				
				/**
				 * assigned cookie
				 */
				Cookie emailCookie = new Cookie("email", email);
				emailCookie.setMaxAge(60 * 60);
				emailCookie.setPath("/");
				resp.addCookie(emailCookie);
				
				/**
				 * message success status 200
				 */
				resp.setStatus(HttpServletResponse.SC_OK); 
				resp.getWriter().write("{\"message\":\"Registration successful\"}");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
