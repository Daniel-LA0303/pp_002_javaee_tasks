package org.mx.project.management.repositories;

import java.util.List;

import org.mx.project.management.models.Project;

public interface ProjectRepository extends GlobalRepository<Project> {

	List<Project> projectsByUser(Long userId);

}
