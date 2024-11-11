package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
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
			stmt.setBoolean(4, false);
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
	public List<Task> taskByProject(Long projectId) {
		List<Task> tasks = new ArrayList<>();
		String sql = "SELECT id, title, description, due_date, status, priority, created_at, updated_at, user_assigned_id, project_id "
				+ "FROM tasks_tbl WHERE project_id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, projectId);
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
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tasks;
	}

	@Override
	public List<Task> tasksByUserAsigned(Long userId) {
		List<Task> tasks = new ArrayList<>();
		String sql = "SELECT id, title, description, due_date, status, priority, created_at, updated_at, user_assigned_id, project_id "
				+ "FROM tasks_tbl WHERE user_assigned_id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, userId);
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
		} catch (SQLException e) {
			e.printStackTrace();
	
		}

		return tasks;
	}

	@Override
	public void update(Task t) throws SQLException {
		
	
	    // SQL de actualización
	    String sql = "UPDATE tasks_tbl SET title = ?, description = ?, due_date = ?, status = ?, priority = ?, updated_at = ?, user_assigned_id = ?, project_id = ? "
	            + "WHERE id = ?";

	    // Comienza a preparar la sentencia SQL
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {

	        // Asigna los valores de los parámetros en el PreparedStatement
	        stmt.setString(1, t.getTitle());
	        stmt.setString(2, t.getDescription());
	        
	        // Verifica si la fecha de vencimiento es válida antes de asignarla
	        if (t.getDueDate() != null) {
	            stmt.setDate(3, Date.valueOf(t.getDueDate()));
	        } else {
	            stmt.setNull(3, Types.DATE);
	        }

	        stmt.setBoolean(4, t.getStatus());
	        stmt.setString(5, t.getPriority());

	        // Agrega la fecha actual de actualización
	        LocalDateTime now = LocalDateTime.now();
	        stmt.setTimestamp(6, Timestamp.valueOf(now));

	        // Asigna los IDs de usuario asignado y proyecto
	        stmt.setLong(7, t.getUserAsignedId());
	        stmt.setLong(8, t.getProjectId());

	        // Asigna el ID de la tarea
	        stmt.setLong(9, t.getId());

	        // Ejecuta la actualización
	        int affectedRows = stmt.executeUpdate();

	        // Si no se afectó ninguna fila, lanza una excepción
	        if (affectedRows == 0) {
	            throw new SQLException("No se encontró la tarea con ID " + t.getId() + " para actualizar.");
	        }

	    } catch (SQLException e) {
	        // Muestra el error en consola y lanza la excepción para que pueda ser manejada en otro lugar
	        System.err.println("Error al actualizar la tarea con ID: " + t.getId());
	        e.printStackTrace();
	        throw new SQLException("Error al actualizar la tarea con ID: " + t.getId(), e);
	    }
	}

	@Override
	public void setTaskStatusToTrue(Long id) {
		 String sql = "UPDATE tasks_tbl SET status = TRUE WHERE id = ?";
		    
		    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		        stmt.setLong(1, id);
		        
		        int rowsAffected = stmt.executeUpdate();
		        
		        if (rowsAffected == 0) {
		            System.out.println("No se encontró una tarea con el ID: " + id);
		        } else {
		            System.out.println("Estado de la tarea con ID " + id + " actualizado a TRUE exitosamente.");
		        }
		    } catch (SQLException e) {
		        System.err.println("Error al intentar actualizar el estado de la tarea con ID " + id);
		        e.printStackTrace();
		        
		    }

	}

	@Override
	public void removeUserFromProject(Long userId, Long projectId) {
		String sql = "UPDATE tasks_tbl SET user_assigned_id = NULL WHERE user_assigned_id = ? AND project_id = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setLong(1, userId);    // Establecemos el userId que será NULL en las tareas
	        stmt.setLong(2, projectId); // Establecemos el projectId para filtrar las tareas del proyecto específico
	        
	        int rowsAffected = stmt.executeUpdate();
	        
	        // Aquí podrías agregar algún manejo de excepciones o lógica adicional si es necesario
	        System.out.println("Número de tareas actualizadas: " + rowsAffected);
	    } catch (SQLException e) {
	        e.printStackTrace();
	       
	    }
		
	}


}
