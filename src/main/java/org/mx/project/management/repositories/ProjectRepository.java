package org.mx.project.management.repositories;

import java.util.List;

import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;

/**
 * project repository
 */
public interface ProjectRepository extends GlobalRepository<Project> {

	List<Project> projectsByUser(Long userId);
	
	List<UserByProjectDTO> getUsersAsignedToProject(Long id);
	
	List<Project> getProjectsByUserAsigned(Long id);
	
	void assignUserToProject(Long userId, Long projectId);
	
	void removeUserFromProject(Long userId, Long projectId);

}
