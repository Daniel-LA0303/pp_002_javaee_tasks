package org.mx.project.management.services.impl;

import java.sql.Connection;
import java.util.Optional;

import org.mx.project.management.models.User;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.UserRepository;
import org.mx.project.management.repositories.impl.UserRepositoryImpl;
import org.mx.project.management.services.UserService;

public class UserServiceImpl implements UserService {

	private GlobalRepository<User> useRepository;

	private UserRepository userRepo;

	public UserServiceImpl(Connection conn) {
		this.useRepository = new UserRepositoryImpl(conn);
		this.userRepo = new UserRepositoryImpl(conn);
	}

	@Override
	public Optional<User> findUserByEmail(String email) {

		return userRepo.findUserByEmail(email);
	}

	@Override
	public void saveUser(User user) {

		try {
			useRepository.save(user);
		} catch (Exception e) {
			// After we work with personal exceptions
		}

	}

}
