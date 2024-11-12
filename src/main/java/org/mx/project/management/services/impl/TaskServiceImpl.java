package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Task;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.TaskRepository;
import org.mx.project.management.repositories.impl.TaskRespositoryImpl;
import org.mx.project.management.services.TaskService;

/**
 * service task
 */
public class TaskServiceImpl implements TaskService {

	private GlobalRepository<Task> tasksRepository;

	private TaskRepository taskRepo;

	/**
	 * @param projectRepository
	 */
	public TaskServiceImpl(Connection connection) {
		this.tasksRepository = new TaskRespositoryImpl(connection);
		this.taskRepo = new TaskRespositoryImpl(connection);
	}

	/**
	 * delete a task
	 */
	@Override
	public String deleteTask(Long id) throws SQLException {
		tasksRepository.delete(id);
		return "Task deleted";
	}

	/**
	 * find all tasks
	 */
	@Override
	public List<Task> findAllTasks() throws SQLException {
		return tasksRepository.findAll();
	}

	/**
	 * find task by project
	 */
	@Override
	public List<Task> findAllTasksBYProjectId(Long projectId) throws SQLException {
		return taskRepo.taskByProject(projectId);
	}

	/**
	 * find tasks by user assigned
	 */
	@Override
	public List<Task> findAllTasksByUserAsigned(Long userId) throws SQLException {
		return taskRepo.tasksByUserAsigned(userId);
	}

	/**
	 * find task by id
	 */
	@Override
	public Task findTaskById(Long id) throws SQLException {
		return tasksRepository.findById(id);
	}

	/**
	 * new task
	 */
	@Override
	public String saveTask(Task task) throws SQLException {
		System.out.println("**********entra a create service");
		tasksRepository.save(task);
		return "Task saved";
	}

	/**
	 * update task
	 */
	@Override
	public String updateTask(Task task) throws SQLException {		
		tasksRepository.update(task);
		return "Task updated";
	}

	/**
	 * change status to true
	 */
	@Override
	public String setTaskStatusToTrue(Long id) throws SQLException {
		taskRepo.setTaskStatusToTrue(id);
		return "Task complete";
	}

	/**
	 * remove user from project
	 */
	@Override
	public void removeUserFromProject(Long userId, Long projectId) throws SQLException {
		taskRepo.removeUserFromProject(userId, projectId);
	}

}
