package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Task;

/**
 * service task
 */
public interface TaskService {

	String deleteTask(Long id) throws SQLException;

	List<Task> findAllTasks() throws SQLException;

	List<Task> findAllTasksBYProjectId(Long projectId) throws SQLException;

	List<Task> findAllTasksByUserAsigned(Long userId) throws SQLException;

	Task findTaskById(Long id) throws SQLException;

	String saveTask(Task task) throws SQLException;

	String updateTask(Task task) throws SQLException;

	String setTaskStatusToTrue(Long id) throws SQLException;
	
	void removeUserFromProject(Long userId, Long projectId) throws SQLException;
}
