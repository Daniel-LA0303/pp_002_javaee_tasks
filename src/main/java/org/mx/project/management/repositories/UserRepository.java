package org.mx.project.management.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.mx.project.management.models.User;

/**
 * user repository
 */
public interface UserRepository extends GlobalRepository<User> {

	Optional<User> findUserByEmail(String email);
	
	List<User> searchUsers(String keyword) throws SQLException;

}
