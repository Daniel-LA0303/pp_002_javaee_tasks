package org.mx.project.management.services;

import java.util.Optional;

import org.mx.project.management.models.User;

public interface UserService {

	Optional<User> findUserByEmail(String email);

	void saveUser(User user);

}
