package org.mx.project.management.filters;

import java.io.IOException;
import java.util.Optional;

import org.mx.project.management.services.AuthService;
import org.mx.project.management.services.impl.AuthServiceImpl;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * we filter this servlets, as a security config
 */
@WebFilter({ "/principal", "/principal.html", "/projects", "/project", "/project.html", "/get-info-by-project" })
public class AuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		AuthService authService = new AuthServiceImpl();

		Optional<String> emailUser = authService.getCookieUser((HttpServletRequest) request);

		if (emailUser.isPresent()) {
			chain.doFilter(request, response); // continue
		} else {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorizated");
		}

	}

}
