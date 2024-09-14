package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Project;

public interface ProjectService {

	List<Project> findAllProjects() throws SQLException;

}
