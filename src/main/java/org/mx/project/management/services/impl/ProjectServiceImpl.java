package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.ProjectRepository;
import org.mx.project.management.repositories.impl.ProjectRepositoryImpl;
import org.mx.project.management.services.ProjectService;

/**
 * service project
 */
public class ProjectServiceImpl implements ProjectService {

	private GlobalRepository<Project> projectRepository;

	private ProjectRepository projectRepo;

	public ProjectServiceImpl(Connection connection) {
		this.projectRepository = new ProjectRepositoryImpl(connection);
		this.projectRepo = new ProjectRepositoryImpl(connection);
	}

	/**
	 * delete a project
	 */
	@Override
	public String deleteProject(Long id) throws SQLException {
		projectRepository.delete(id);
		return "Project deleted";
	}

	/**
	 * find all projects
	 */
	@Override
	public List<Project> findAllProjects() throws SQLException {
		return projectRepository.findAll();
	}

	/**
	 * projects by user id
	 */
	@Override
	public List<Project> findAllProjectsByUserId(Long userId) throws SQLException {
		return projectRepo.projectsByUser(userId);
	}

	/**
	 * find by id
	 */
	@Override
	public Project findProjectById(Long id) throws SQLException {
		return projectRepo.findById(id);
	}

	/**
	 * new project
	 */
	@Override
	public String saveProject(Project project) throws SQLException {
		projectRepository.save(project);
		return "Poject saved";
	}

	/**
	 * update project 
	 */
	@Override
	public String updateProject(Project project) throws SQLException {
		projectRepository.update(project);
		return "Project updated";
	}

	/**
	 * get users assigned to project
	 */
	@Override
	public List<UserByProjectDTO> getUsersAsignedToProject(Long id) throws SQLException {
		return projectRepo.getUsersAsignedToProject(id);
	}

	/**
	 * get projects by user assigned
	 */
	@Override
	public List<Project> getProjectsByUserAigned(Long id) throws SQLException {
		return projectRepo.getProjectsByUserAsigned(id);
	}

	/**
	 * assigned a user to project
	 */
	@Override
	public void assignUserToProject(Long userId, Long projectId) throws SQLException {
		projectRepo.assignUserToProject(userId, projectId);
	}

	/**
	 * remove user from project
	 */
	@Override
	public void removeUserFromProject(Long userId, Long projectId) throws SQLException {
		projectRepo.removeUserFromProject(userId, projectId);
	}

}
