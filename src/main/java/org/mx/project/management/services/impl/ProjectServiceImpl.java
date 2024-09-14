package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Project;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.impl.ProjectRepositoryImpl;
import org.mx.project.management.services.ProjectService;

public class ProjectServiceImpl implements ProjectService {

	private GlobalRepository<Project> projectRepository;

	public ProjectServiceImpl(Connection connection) {
		this.projectRepository = new ProjectRepositoryImpl(connection);
	}

	@Override
	public List<Project> findAllProjects() throws SQLException {
		return projectRepository.findAll();
	}

}
