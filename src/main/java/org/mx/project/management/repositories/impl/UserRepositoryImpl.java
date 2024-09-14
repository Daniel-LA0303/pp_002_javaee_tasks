package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mx.project.management.models.User;
import org.mx.project.management.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {

	private Connection connection;

	public UserRepositoryImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM users_tbl WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, id);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected == 0) {
				throw new SQLException("No user found with ID: " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<User> findAll() throws SQLException {
		String sql = "SELECT * FROM users_tbl";

		List<User> users = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
				user.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return users;
	}

	@Override
	public User findById(Long id) throws SQLException {
		String sql = "SELECT * FROM users_tbl WHERE id = ?";

		User user = null;

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					user = new User();
					user.setId(rs.getLong("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					user.setEmail(rs.getString("email"));
					user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
					user.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return user;
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		String sql = "select * from users_tbl where email = ?";

		User user = null;

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, email);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					user = new User();
					user.setName(rs.getString("name"));
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
					user.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));

				}
			}

		} catch (Exception e) {

		}

		return Optional.ofNullable(user);
	}

	@Override
	public void save(User user) throws SQLException {

		String sql = "insert into users_tbl (name, password, email, created_at, updated_at) values (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());

			LocalDateTime nowTime = LocalDateTime.now();

			stmt.setTimestamp(4, Timestamp.valueOf(nowTime));
			stmt.setTimestamp(5, Timestamp.valueOf(nowTime));

			int rowsAffected = stmt.executeUpdate();
			System.out.println(rowsAffected);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void update(User user) throws SQLException {
		String sql = "UPDATE users_tbl SET name = ?, password = ?, email = ?, updated_at = ? WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());

			LocalDateTime now = LocalDateTime.now();
			stmt.setTimestamp(4, Timestamp.valueOf(now));

			stmt.setLong(5, user.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
