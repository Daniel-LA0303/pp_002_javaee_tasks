package org.mx.project.management.services;

import java.sql.SQLException;
import java.util.List;

import org.mx.project.management.models.Task;

public interface TaskService {

	List<Task> findAllTasks() throws SQLException;

}
