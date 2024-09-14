package org.mx.project.management.repositories;

import java.util.Optional;

import org.mx.project.management.models.User;

public interface UserRepository extends GlobalRepository<User> {

	Optional<User> findUserByEmail(String email);

}
