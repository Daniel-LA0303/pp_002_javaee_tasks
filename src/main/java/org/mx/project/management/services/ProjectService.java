package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Project;

public interface ProjectService {

	String deleteProject(Long id) throws SQLException;

	List<Project> findAllProjects() throws SQLException;

	List<Project> findAllProjectsByUserId(Long userId) throws SQLException;

	Project findProjectById(Long id) throws SQLException;

	String saveProject(Project project) throws SQLException;

	String updateProject(Project project) throws SQLException;

}
