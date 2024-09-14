package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Task;

public interface TaskService {

	String deleteTask(Long id) throws SQLException;

	List<Task> findAllTasks() throws SQLException;

	Task findTaskById(Long id) throws SQLException;

	String saveTask(Task task) throws SQLException;

	String updateTask(Task task) throws SQLException;

}
