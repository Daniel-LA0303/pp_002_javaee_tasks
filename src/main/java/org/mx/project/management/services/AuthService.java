package org.mx.project.management.services;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * service auth
 */
public interface AuthService {

	public Optional<String> getCookieUser(HttpServletRequest request);
}
