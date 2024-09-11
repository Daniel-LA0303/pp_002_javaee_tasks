package org.mx.project.management.services.impl;

import java.sql.Connection;

import org.mx.project.management.models.User;
import org.mx.project.management.repositories.GlobalRepository;
import org.mx.project.management.repositories.impl.UserRepositoryImpl;
import org.mx.project.management.services.UserService;

public class UserServiceImpl implements UserService {

	private GlobalRepository<User> useRepository;

	public UserServiceImpl(Connection conn) {
		this.useRepository = new UserRepositoryImpl(conn);
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
