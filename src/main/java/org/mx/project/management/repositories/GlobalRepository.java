package org.mx.project.management.repositories;

import java.sql.SQLException;
import java.util.List;

/**
 * global repository to create personal repositories
 * @param <T>
 */
public interface GlobalRepository<T> {

	void delete(Long id) throws SQLException;

	List<T> findAll() throws SQLException;

	T findById(Long id) throws SQLException;

	void save(T t) throws SQLException;

	void update(T t) throws SQLException;

}
