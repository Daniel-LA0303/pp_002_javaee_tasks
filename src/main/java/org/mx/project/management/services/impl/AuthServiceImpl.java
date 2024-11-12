package org.mx.project.management.services.impl;

import java.util.Arrays;
import java.util.Optional;

import org.mx.project.management.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class AuthServiceImpl implements AuthService {

	/**
	 * we get cookie
	 */
	@Override
	public Optional<String> getCookieUser(HttpServletRequest request) {

		Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];
		return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("email")).map(Cookie::getValue)
				.findAny();

	}

}
