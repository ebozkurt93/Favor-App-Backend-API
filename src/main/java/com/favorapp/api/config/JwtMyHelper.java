package com.favorapp.api.config;

import com.favorapp.api.user.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtMyHelper {

	public static boolean getJWTUser(String jwt) {

		jwt = jwt.replace("Bearer ", "");
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwt).getBody();
		Role role = (Role) claims.get("roles");
		if (role.toString().equals("USER")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean getJWTAdmin(String jwt) {

		jwt = jwt.replace("Bearer ", "");
		Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwt).getBody();
		Object role = claims.get("roles");
		if (role.equals("ADMIN")) {
			return true;
		} else {
			return false;
		}
	}

}
