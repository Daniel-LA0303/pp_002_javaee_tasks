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

import org.apache.taglibs.standard.tag.el.sql.SetDataSourceTag;
import org.mx.project.management.dto.UserByProjectDTO;
import org.mx.project.management.models.Project;
import org.mx.project.management.repositories.ProjectRepository;

/**
 * implementation project
 */
public class ProjectRepositoryImpl implements ProjectRepository {

	private Connection connection;

	public ProjectRepositoryImpl(Connection connection) {
		this.connection = connection;
	}

	/**
	 * delete a project
	 */
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

	/**
	 * find all projects
	 */
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

	/**
	 * find by id 
	 */
	@Override
	public Project findById(Long id) throws SQLException {

		String sql = "SELECT * FROM projects_tbl WHERE id = ?";

		Project project = null;

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
			throw e;
		}
		return project;
	}

	/**
	 * get all projects by user
	 */
	@Override
	public List<Project> projectsByUser(Long userId) {
		List<Project> projects = new ArrayList<>();
		String sql = "SELECT id, title, description, start_date, end_date, status, created_at, updated_at, user_id "
				+ "FROM projects_tbl WHERE user_id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Project project = new Project();
					project.setId(rs.getLong("id"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setStartDate(rs.getDate("start_date").toLocalDate());
					project.setEndDate(rs.getDate("end_date").toLocalDate());
					project.setStatus(rs.getBoolean("status"));
					project.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
					project.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
					project.setUserId(rs.getLong("user_id"));
					projects.add(project);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return projects;
	}

	/**
	 * new project
	 */
	@Override
	public void save(Project project) throws SQLException {

		String sql = "INSERT INTO projects_tbl (title, description, start_date, end_date, status, created_at, updated_at, user_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, project.getTitle());
			stmt.setString(2, project.getDescription());

			stmt.setDate(3, Date.valueOf(project.getStartDate()));
			stmt.setDate(4, Date.valueOf(project.getEndDate()));
			stmt.setBoolean(5, false);

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(6, Timestamp.valueOf(now));
			stmt.setTimestamp(7, Timestamp.valueOf(now));
			stmt.setLong(8, project.getUserId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * update project
	 */
	@Override
	public void update(Project project) throws SQLException {

		String sql = "UPDATE projects_tbl SET title = ?, description = ?, start_date = ?, end_date = ?, status = ?, updated_at = ?, user_id = ? "
				+ "WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, project.getTitle());
			stmt.setString(2, project.getDescription());

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

	/**
	 * get all users assigned to project
	 */
	@Override
	public List<UserByProjectDTO> getUsersAsignedToProject(Long id) {
		String sql = """
	            SELECT u.id, u.name, u.email 
	            FROM users_tbl u
	            JOIN users_projects_tbl up ON u.id = up.user_id
	            JOIN projects_tbl p ON p.id = up.project_id
	            WHERE p.id = ?;
	            """;
	    
	    List<UserByProjectDTO> users = new ArrayList<>();

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	               
	                Long projectId = rs.getLong("id");
	                String name = rs.getString("name");
	                String email = rs.getString("email");

	                users.add(new UserByProjectDTO(projectId, name, email));
	            }
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return users;
	}

	/**
	 * get all projects by user assigned
	 */
	@Override
	public List<Project> getProjectsByUserAsigned(Long id) {
	    String sql = """
	            SELECT p.id, p.title, p.description, p.start_date, p.end_date, p.status, p.created_at, p.updated_at, p.user_id
	            FROM projects_tbl p
	            JOIN users_projects_tbl up ON p.id = up.project_id
	            WHERE up.user_id = ?;
	            """;

	    List<Project> projects = new ArrayList<>();

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        
	        // Establecemos el parámetro user_id
	        stmt.setLong(1, id);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                
					Project project = new Project();
					project.setId(rs.getLong("id"));
					project.setTitle(rs.getString("title"));
					project.setDescription(rs.getString("description"));
					project.setStartDate(rs.getDate("start_date").toLocalDate());
					project.setEndDate(rs.getDate("end_date").toLocalDate());
					project.setStatus(rs.getBoolean("status"));
					project.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
					project.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
					project.setUserId(rs.getLong("user_id"));
					projects.add(project);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return projects;
	}

	/**
	 * assigned one user to one project
	 */
	@Override
	public void assignUserToProject(Long userId, Long projectId) {
		String sql = "INSERT INTO users_projects_tbl (user_id, project_id, assigned_at) "
	               + "VALUES (?, ?, ?)";

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setLong(1, userId); 
	        stmt.setLong(2, projectId); 
	        stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();

	    }
		
	}

	/**
	 * remove one user assigned from a project
	 */
	@Override
	public void removeUserFromProject(Long userId, Long projectId) {
		String sql = "DELETE FROM users_projects_tbl WHERE user_id = ? AND project_id = ?";

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {     
	        stmt.setLong(1, userId);  
	        stmt.setLong(2, projectId);  
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	}


}
