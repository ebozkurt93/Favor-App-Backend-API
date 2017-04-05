package com.favorapp.api.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public void addNewUser(@RequestBody User u) {
		//check if email is valid
		String email = u.getEmail();
		if (!userService.isValidEmailAddress(email))
		{
			throw new IllegalArgumentException("The email address is not valid");
		}
		//check if email is used
		else if(!userService.checkIfEmailUsed(email)) {
			Date date = new Date();
			u.setRegisterDate(date);
			userService.addUser(u);
		} else
			throw new IllegalArgumentException("The email address is already in use");
	}

	// TODO disable this for release
	@RequestMapping(value = "/all")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@RequestMapping(value = "/{id}")
	public User getUser(@PathVariable int id) {
		return userService.getUser(id);
	}
}
