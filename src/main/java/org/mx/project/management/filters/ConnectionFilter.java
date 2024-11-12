package org.mx.project.management.filters;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.mx.project.management.config.ConexionDB;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * this filter help us to not duplicate conexion to db
 */
@WebFilter("/*")
public class ConnectionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

        /**
         * Establishes a database connection from the connection pool (ConexionDBDS),
         * disables auto-commit for transaction management, and makes it available 
         * for subsequent filters or servlets.
         */
		try (Connection conn = ConexionDB.getConnection()) {
			System.out.println("Filtro ejecutado. Conn: " + conn);

			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			}
			// Pass control on to the next filter
			try {
				servletRequest.setAttribute("conn", conn);
				filterChain.doFilter(servletRequest, servletResponse);
				conn.commit();
			} catch (SQLException | ServletException e) {
				conn.rollback();
				((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
				e.printStackTrace();
			}

		} catch (SQLException | NamingException throwables) {
			throwables.printStackTrace();
		}
	}

}
