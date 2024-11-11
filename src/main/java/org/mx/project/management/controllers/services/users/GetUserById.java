package org.mx.project.management.controllers.services.users;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import org.mx.project.management.config.dateConfig.GsonConfig;
import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;
import org.mx.project.management.models.User;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.UserServiceImpl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/user-by-id" })
public class GetUserById extends HttpServlet {

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
		UserService userService = new UserServiceImpl(conn);
		System.out.println("invocado");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JsonObject jsonObject = JsonUtils.parseJsonRequest(req);
		// Long userId = jsonObject.get("userId").getAsLong();
		Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong() : null;

		System.out.println("userId" + userId);

		try {
			User user = userService.findUserBYId(userId);
			if (user == null) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				resp.setContentType("application/json");
				resp.getWriter().write("{\"message\":\"User not found\"}");
				return;
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");

			Gson gson = GsonConfig.createGson();
			String json = gson.toJson(user);
			resp.getWriter().write(json);
		} catch (Exception e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().write("{\"error\": \"Database error occurred\"}");
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = (Connection) req.getAttribute("conn");
	    UserService userService = new UserServiceImpl(conn);
	    ProjectService projectService = new ProjectServiceImpl(conn);
	    
	    
	    
	    resp.setContentType("application/json");
	    resp.setCharacterEncoding("UTF-8");

	    JsonObject jsonObject = JsonUtils.parseJsonRequest(req);  

	    String keyword = jsonObject.has("keyword") ? jsonObject.get("keyword").getAsString() : null;
	    Long projectId = jsonObject.has("projectId") ? jsonObject.get("projectId").getAsLong(): null;
	    Long userId = jsonObject.has("userId") ? jsonObject.get("userId").getAsLong(): null;

	    System.out.println(userId + " " + projectId);
	    
	    if (keyword == null || keyword.trim().isEmpty()) {
	        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        resp.getWriter().write("{\"message\":\"Keyword is required for searching.\"}");
	        return;
	    }

	    try {
	    	
	    	List<UserByProjectDTO> usersAssigned = projectService.getUsersAsignedToProject(projectId);
	    	
	        List<User> users = userService.searchUsers(keyword);
	        
	        List<User> filteredUsers = users.stream()
	        	    .filter(user -> !user.getId().equals(userId))  
	        	    .filter(user -> usersAssigned.stream().noneMatch(assignedUser -> assignedUser.getId().equals(user.getId())))  
	        	    .collect(Collectors.toList());

	        if (users.isEmpty()) {
	            resp.setStatus(HttpServletResponse.SC_OK);
	            resp.getWriter().write("{\"message\":\"No users found.\"}");
	            return;
	        }

	        JsonArray jsonUsers = new JsonArray();
	        for (User user : filteredUsers) {
	            JsonObject jsonUser = new JsonObject();
	            jsonUser.addProperty("id", user.getId());
	            jsonUser.addProperty("name", user.getName());
	            jsonUser.addProperty("email", user.getEmail());
	            jsonUser.addProperty("createdAt", user.getCreatedAt().toString());
	            jsonUser.addProperty("updatedAt", user.getUpdatedAt().toString());
	            jsonUsers.add(jsonUser);
	        }

	        System.out.println(jsonUsers);
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write(jsonUsers.toString());
	    } catch (Exception e) {
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while searching for users.\"}");
	        e.printStackTrace();
	    }
	}
	
	

}
