package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
		String sql = "DELETE FROM tasks_tbl WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, id);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
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

					tasks.add(task);
				}
			}

		} catch (Exception e) {

		}

		return tasks;
	}

	@Override
	public Task findById(Long id) throws SQLException {
		String sql = "SELECT * FROM tasks_tbl WHERE id = ?";
		Task task = null;

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					task = new Task();
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
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return task;
	}

	@Override
	public void save(Task t) throws SQLException {
		String sql = "INSERT INTO tasks_tbl (title, description, due_date, status, priority, created_at, updated_at, user_assigned_id, project_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, t.getTitle());
			stmt.setString(2, t.getDescription());
			stmt.setDate(3, Date.valueOf(t.getDueDate()));
			stmt.setBoolean(4, t.getStatus());
			stmt.setString(5, t.getPriority());

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(6, Timestamp.valueOf(now));
			stmt.setTimestamp(7, Timestamp.valueOf(now));

			stmt.setLong(8, t.getUserAsignedId());
			stmt.setLong(9, t.getProjectId());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						t.setId(generatedKeys.getLong(1));
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void update(Task t) throws SQLException {
		String sql = "UPDATE tasks_tbl SET title = ?, description = ?, due_date = ?, status = ?, priority = ?, updated_at = ?, user_assigned_id = ?, project_id = ? "
				+ "WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, t.getTitle());
			stmt.setString(2, t.getDescription());
			stmt.setDate(3, Date.valueOf(t.getDueDate()));
			stmt.setBoolean(4, t.getStatus());
			stmt.setString(5, t.getPriority());

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(6, Timestamp.valueOf(now));

			stmt.setLong(7, t.getUserAsignedId());
			stmt.setLong(8, t.getProjectId());

			stmt.setLong(9, t.getId());

			int affectedRows = stmt.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("No se encontró la tarea con ID " + t.getId() + " para actualizar.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
