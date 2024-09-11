package org.mx.project.management.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.mx.project.management.models.User;
import org.mx.project.management.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {

	private Connection connection;

	public UserRepositoryImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void delete(Long id) throws SQLException {

	}

	@Override
	public List<User> findAll() throws SQLException {

		return null;
	}

	@Override
	public User findById(Long id) throws SQLException {

		return null;
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
			e.printStackTrace(); // Manejo de excepciones
			throw e;
		}

	}

	@Override
	public void update(User t) throws SQLException {

	}

}
