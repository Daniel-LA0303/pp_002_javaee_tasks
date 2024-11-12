package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.mx.project.management.models.User;

/**
 * service user
 */
public interface UserService {

	String deleteUser(Long id) throws SQLException;

	List<User> findAllUsers() throws SQLException;

	Optional<User> findUserByEmail(String email) throws SQLException;

	User findUserBYId(Long id) throws SQLException;

	String saveUser(User user) throws SQLException;

	String updateUser(User user) throws SQLException;
	
	List<User> searchUsers(String keyword) throws SQLException;

}
