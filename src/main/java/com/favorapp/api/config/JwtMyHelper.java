package com.favorapp.api.config;

import java.util.ArrayList;

import javax.servlet.ServletException;

import com.favorapp.api.config.key.KeyFactory;
import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class JwtMyHelper {

	private UserService userService;

	public JwtMyHelper() {
	}

	public JwtMyHelper(UserService userService) {
		this.userService = userService;
	}

	public static ArrayList<String> getJWTRoles(String jwt) {
		if (KeyFactory.checkKeyValidity(jwt)) {
			jwt = jwt.replace("Bearer ", "");
			Claims claims = Jwts.parser().setSigningKey(KeyFactory.jwtKey).parseClaimsJws(jwt).getBody();
			@SuppressWarnings(value = {"unchecked" })
			ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
			return roles;
			/*
			if (!(roles.contains("USER") || roles.contains("ADMIN") || roles.contains("BLOCKED"))) {
				throw new ServletException("testing exception");
			}
			if(roles.isEmpty()) {
				throw new ServletException("testing is empty exception");
			}
			*/
		}
		return null;
	}

	public static boolean getIfJWTUser(String jwt)  {
		ArrayList<String> roleList = getJWTRoles(jwt);
		if (roleList.contains("USER")) {
			return true;
		} else {
			return false;
		}
	}

	public User getUserFromJWT(String jwt) {
		jwt = jwt.replace("Bearer ", "");
		String email = Jwts.parser().setSigningKey(KeyFactory.jwtKey).parseClaimsJws(jwt).getBody().getSubject();
		return userService.getUserByEmail(email);
	}

	public static boolean getIfJWTAdmin(String jwt)  {
		ArrayList<String> roleList = getJWTRoles(jwt);
		if (roleList.contains("ADMIN")) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * public static boolean getJWTAdmin(String jwt) {
	 * 
	 * jwt = jwt.replace("Bearer ", ""); Claims claims =
	 * Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwt).getBody();
	 * Object role = claims.get("roles"); if (role.equals("ADMIN")) { return
	 * true; } else { return false; } }
	 */
}
