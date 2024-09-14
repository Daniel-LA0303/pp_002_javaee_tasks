package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Task;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.impl.TaskRespositoryImpl;
import org.mx.project.management.services.TaskService;

public class TaskServiceImpl implements TaskService {

	private GlobalRepository<Task> tasksRepository;

	/**
	 * @param projectRepository
	 */
	public TaskServiceImpl(Connection connection) {
		this.tasksRepository = new TaskRespositoryImpl(connection);
	}

	@Override
	public String deleteTask(Long id) throws SQLException {
		tasksRepository.delete(id);
		return "Task deleted";
	}

	@Override
	public List<Task> findAllTasks() throws SQLException {

		return tasksRepository.findAll();
	}

	@Override
	public Task findTaskById(Long id) throws SQLException {

		return tasksRepository.findById(id);
	}

	@Override
	public String saveTask(Task task) throws SQLException {
		tasksRepository.save(task);
		return "Task saved";
	}

	@Override
	public String updateTask(Task task) throws SQLException {
		tasksRepository.update(task);
		return "Task updated";
	}

}
