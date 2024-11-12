package org.mx.project.management.controllers.services.users;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import org.mx.project.management.config.jsonConverter.JsonUtils;
import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.User;
import org.mx.project.management.services.ProjectService;
import org.mx.project.management.services.UserService;
import org.mx.project.management.services.impl.ProjectServiceImpl;
import org.mx.project.management.services.impl.UserServiceImpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({ "/search-user" })
public class SearchUsersServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	        	// status 200 array is empty 
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

	        // status 200
	        resp.setStatus(HttpServletResponse.SC_OK);
	        resp.getWriter().write(jsonUsers.toString());
	    } catch (Exception e) {
	    	// status 500
	        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        resp.getWriter().write("{\"message\":\"An error occurred while searching for users.\"}");
	        e.printStackTrace();
	    }
	}
	
	

}
