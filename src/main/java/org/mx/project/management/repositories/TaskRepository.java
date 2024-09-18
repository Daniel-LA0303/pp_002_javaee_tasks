package org.mx.project.management.repositories;

import java.util.List;

import org.mx.project.management.models.Task;

public interface TaskRepository extends GlobalRepository<Task> {

	List<Task> taskByProject(Long projectId);

	List<Task> tasksByUserAsigned(Long userId);

}
