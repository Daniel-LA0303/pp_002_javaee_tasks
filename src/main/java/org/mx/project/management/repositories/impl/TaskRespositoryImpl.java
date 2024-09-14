package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mx.project.management.models.Task;
import org.mx.project.management.repositories.TaskRepository;

public class TaskRespositoryImpl implements TaskRepository {

	private Connection connection;

	/**
	 * @param connection
	 */
	public TaskRespositoryImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void delete(Long id) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Task> findAll() throws SQLException {

		String sql = "select * from tasks_tbl";

		List<Task> tasks = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Task task = new Task();
					task.setId(rs.getLong("id"));
					task.setTitle(rs.getString("title"));
					task.setDescription(rs.getString("description"));
					task.setDueDate(rs.getDate("due_date").toLocalDate());
					task.setStatus(rs.getBoolean("status"));
					task.setPriority(rs.getString("priority"));
					task.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
					task.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
					task.setUserAsignedId(rs.getLong("user_assigned_id"));
					task.setProjectId(rs.getLong("project_id"));

					// Agrega el objeto Task a la lista de tareas
					tasks.add(task);
				}
			}

		} catch (Exception e) {

		}

		return tasks;
	}

	@Override
	public Task findById(Long id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Task t) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Task t) throws SQLException {
		// TODO Auto-generated method stub

	}

}
