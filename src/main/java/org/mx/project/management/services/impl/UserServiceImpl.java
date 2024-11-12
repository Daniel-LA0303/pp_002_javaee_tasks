package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.mx.project.management.models.User;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.UserRepository;
import org.mx.project.management.repositories.impl.UserRepositoryImpl;
import org.mx.project.management.services.UserService;

/**
 * user service
 */
public class UserServiceImpl implements UserService {

	private GlobalRepository<User> useRepository;

	private UserRepository userRepo;

	public UserServiceImpl(Connection conn) {
		this.useRepository = new UserRepositoryImpl(conn);
		this.userRepo = new UserRepositoryImpl(conn);
	}

	/**
	 * delete user
	 */
	@Override
	public String deleteUser(Long id) throws SQLException {
		useRepository.delete(id);
		return "User deleted";
	}

	/**
	 * find all users
	 */
	@Override
	public List<User> findAllUsers() throws SQLException {
		return useRepository.findAll();
	}

	/**
	 * find user by email
	 */
	@Override
	public Optional<User> findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}

	/**
	 * find user by id
	 */
	@Override
	public User findUserBYId(Long id) throws SQLException {

		return useRepository.findById(id);
	}

	/**
	 * new user
	 */
	@Override
	public String saveUser(User user) throws SQLException {
		useRepository.save(user);
		return "User saved";
	}

	/**
	 * update user
	 */
	@Override
	public String updateUser(User user) throws SQLException {
		useRepository.update(user);
		return "User updated";
	}

	/**
	 * search users
	 */
	@Override
	public List<User> searchUsers(String keyword) throws SQLException {
		return userRepo.searchUsers(keyword);
	}

}
