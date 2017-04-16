package com.favorapp.api.config;

import java.util.ArrayList;

import javax.servlet.ServletException;

import com.favorapp.api.config.key.KeyFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtMyHelper {

	public static ArrayList<String> getJWTRoles(String jwt) {
		if (KeyFactory.checkKeyValidity(jwt)) {
			jwt = jwt.replace("Bearer ", "");
			Claims claims = Jwts.parser().setSigningKey(KeyFactory.jwtKey).parseClaimsJws(jwt).getBody();
			ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
			return roles;
		}
		try {// TODO find a solution for this null pointer issue related to here

			throw new ServletException("This key is not valid anymore");
		} catch (ServletException e) {
		}
		return null;
	}

	public static boolean getIfJWTUser(String jwt) {
		ArrayList<String> roleList = getJWTRoles(jwt);
		if (roleList.contains("USER")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean getIfJWTAdmin(String jwt) {
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
