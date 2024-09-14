package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Project;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.ProjectRepository;
import org.mx.project.management.repositories.impl.ProjectRepositoryImpl;
import org.mx.project.management.services.ProjectService;

public class ProjectServiceImpl implements ProjectService {

	private GlobalRepository<Project> projectRepository;

	private ProjectRepository projectRepo;

	public ProjectServiceImpl(Connection connection) {
		this.projectRepository = new ProjectRepositoryImpl(connection);
		this.projectRepo = new ProjectRepositoryImpl(connection);
	}

	@Override
	public String deleteProject(Long id) throws SQLException {
		projectRepository.delete(id);
		return "Project deleted";
	}

	@Override
	public List<Project> findAllProjects() throws SQLException {
		return projectRepository.findAll();
	}

	@Override
	public Project findProjectById(Long id) throws SQLException {

		return projectRepo.findById(id);
	}

	@Override
	public String saveProject(Project project) throws SQLException {

		projectRepository.save(project);

		return "Poject saved";
	}

	@Override
	public String updateProject(Project project) throws SQLException {
		projectRepository.update(project);
		return "Project updated";
	}

}
