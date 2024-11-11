package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;

public interface ProjectService {

	String deleteProject(Long id) throws SQLException;

	List<Project> findAllProjects() throws SQLException;

	List<Project> findAllProjectsByUserId(Long userId) throws SQLException;

	Project findProjectById(Long id) throws SQLException;

	String saveProject(Project project) throws SQLException;

	String updateProject(Project project) throws SQLException;
	
	List<UserByProjectDTO> getUsersAsignedToProject(Long id) throws SQLException;
	
	List<Project> getProjectsByUserAigned(Long id) throws SQLException;
	
	void assignUserToProject(Long userId, Long projectId) throws SQLException;
	
	void removeUserFromProject(Long userId, Long projectId) throws SQLException;

}
