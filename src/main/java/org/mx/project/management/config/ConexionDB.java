package org.mx.project.management.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexionDB {

	public static Connection getConnection() throws SQLException, NamingException {

		Context initContext = null;
		DataSource ds = null;

		initContext = new InitialContext();

		Context envContext = (Context) initContext.lookup("java:/comp/env");
		ds = (DataSource) envContext.lookup("jdbc/mySQLDB");
		return ds.getConnection();

	}

}
