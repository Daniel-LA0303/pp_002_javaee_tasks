package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub

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
			// TODO: handle exception
		}

		return projects;
	}

	@Override
	public Project findById(Long id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Project t) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Project t) throws SQLException {
		// TODO Auto-generated method stub

	}

}
