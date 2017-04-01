package com.favorapp.api.user;

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
	public void addNewUser(@RequestBody User u ) {
		userService.addUser(u);
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

	/*
	 * @RequestMapping("/demo")
	 * 
	 * @ResponseBody public String demo() { //User u = new
	 * User(name,lastName,birthDate,phoneNumber); User u = new User();
	 * u.setName("name"); u.setLastName("lastName");
	 * u.setBirthDate("birthDate"); u.setPhoneNumber("phoneNumber");
	 * userRepository.save(u); return "Saved"; }
	 */
}
