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
	public List<Task> findAllTasks() throws SQLException {

		return tasksRepository.findAll();
	}

}
