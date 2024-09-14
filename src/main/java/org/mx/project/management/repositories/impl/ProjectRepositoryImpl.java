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

import org.mx.project.management.models.Project;
import org.mx.project.management.repositories.ProjectRepository;

public class ProjectRepositoryImpl implements ProjectRepository {

	private Connection connection;

	/**
	 * 
	 */
	public ProjectRepositoryImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM projects_tbl WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Project> findAll() throws SQLException {

		String sql = "select * from projects_tbl";

		List<Project> projects = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Project project = new Project();
					project.setId(rs.getLong("id"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setStartDate(rs.getDate("start_date").toLocalDate());
					project.setEndDate(rs.getDate("end_date").toLocalDate());
					project.setStatus(rs.getBoolean("status"));
					project.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
					project.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
					project.setUserId(rs.getLong("user_id"));

					projects.add(project);
				}

			}

		} catch (Exception e) {

		}

		return projects;
	}

	@Override
	public Project findById(Long id) throws SQLException {

		String sql = "SELECT * FROM projects_tbl WHERE id = ?";

		Project project = null;

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			// Establecer el parámetro del ID
			stmt.setLong(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					project = new Project();
					project.setId(rs.getLong("id"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setStartDate(rs.getDate("start_date").toLocalDate());
					project.setEndDate(rs.getDate("end_date").toLocalDate());
					project.setStatus(rs.getBoolean("status"));
					project.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
					project.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
					project.setUserId(rs.getLong("user_id"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e; // Lanzar la excepción para que el llamador pueda manejarla
		}

		return project;
	}

	@Override
	public void save(Project project) throws SQLException {

		String sql = "INSERT INTO projects_tbl (title, description, start_date, end_date, status, created_at, updated_at, user_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, project.getTitle());
			stmt.setString(2, project.getDescription());

			stmt.setDate(3, Date.valueOf(project.getStartDate()));
			stmt.setDate(4, Date.valueOf(project.getEndDate()));
			stmt.setBoolean(5, project.getStatus());

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(6, Timestamp.valueOf(now));
			stmt.setTimestamp(7, Timestamp.valueOf(now));
			stmt.setLong(8, project.getUserId());

			stmt.executeUpdate();

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					project.setId(generatedKeys.getLong(1));
					System.out.println("Id generated: " + generatedKeys.getLong(1));

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void update(Project project) throws SQLException {

		String sql = "UPDATE projects_tbl SET title = ?, description = ?, start_date = ?, end_date = ?, status = ?, updated_at = ?, user_id = ? "
				+ "WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			// Establecer los parámetros
			stmt.setString(1, project.getTitle());
			stmt.setString(2, project.getDescription());

			// Convertir LocalDate a java.sql.Date
			stmt.setDate(3, Date.valueOf(project.getStartDate()));
			stmt.setDate(4, Date.valueOf(project.getEndDate()));

			stmt.setBoolean(5, project.getStatus());

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(6, Timestamp.valueOf(now));

			stmt.setLong(7, project.getUserId());
			stmt.setLong(8, project.getId());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
