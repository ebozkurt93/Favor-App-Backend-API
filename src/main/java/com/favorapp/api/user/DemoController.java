package com.favorapp.api.user;

import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.favorapp.api.config.JwtMyHelper;

@RestController
@RequestMapping(path = "/demo/")
public class DemoController {
	
	@Autowired
	private UserService userService;
	/*
	@RequestMapping(value = "/secure/all")
	public List<User> getAllUsers(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			return userService.getAllUsers();
		} else {
			throw new ServletException("You are not authorized to do that");
		}
	}
*/
}
