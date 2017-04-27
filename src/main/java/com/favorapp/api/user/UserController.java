package com.favorapp.api.user;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.MailSenderService;
import com.favorapp.api.config.key.KeyFactory;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(path = "/user/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private MailSenderService mailSenderService;

	@RequestMapping(method = RequestMethod.GET, value = "/hello")
	public String hello() {
		return "Working";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/emaildemo")
	public void emailDemo() throws MailException, InterruptedException {
		User user = new User();
		user.setEmail("favorapp2017@gmail.com");

		mailSenderService.sendEmail(user);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public void addNewUser(@RequestBody User u) throws ServletException {
		// check if email is valid
		String email = u.getEmail();
		if (!userService.isValidEmailAddress(email)) {
			throw new ServletException("The email address is not valid");
		}
		// check if email is used
		else if (!userService.checkIfEmailUsed(email)) {
			Date date = new Date();
			u.setRegisterDate(date);
			u.getRoles().add(Role.USER);

			/*
			 * 
			 * int hash = email.hashCode(); String msg = String.valueOf(hash);
			 * emailSender.sendMail("Favor", "favorapp2017@gmail.com",
			 * "Favor Registration", msg);
			 */

			//
			userService.addUser(u);

			
			int hash = email.hashCode();
			String emailLink = String.valueOf(hash);
			String finalLink = "http://localhost:8080/user/?validate_user"; //continue from here
			String emailaddress = "favorapp2017@gmail.com";
			String subject = "Welcome to Boon";
			String text = "link is " + finalLink;
			try {
				mailSenderService.sendEmailWithDetails(emailaddress, subject, text);
			} catch (MailException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else
			throw new ServletException("The email address is already in use");
	}

	@RequestMapping(value = "/secure/all")
	public List<User> getAllUsers(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt) && KeyFactory.checkKeyValidity(jwt)) {
			return userService.getAllUsers();
		} else {
			throw new ServletException("You are not authorized to do that");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/getbyid/")
	public User getUserById(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
			throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			int id = u.getId();
			User user = userService.getUserById(id);
			if (user == null) {
				throw new ServletException("No user with that id");
			}
			return user;
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/getbyemail/")
	public User getUserByEmail(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
			throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			String email = u.getEmail();
			User user = userService.getUserByEmail(email);
			if (user == null) {
				throw new ServletException("No user with that email");
			}
			return user;
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public String login(@RequestBody User login) throws ServletException {

		String jwtToken = "";
		if (login.getEmail() == null || login.getPassword() == null) {
			throw new ServletException("Please fill in username and password");
		}

		String email = login.getEmail();
		String password = login.getPassword();

		User user = userService.getUserByEmail(email);

		if (user == null) {
			throw new ServletException("User email not found.");
		}

		String pwd = user.getPassword();

		if (!password.equals(pwd)) {
			throw new ServletException("Invalid login. Please check your email and password.");
		}

		if (userService.getUserByEmail(email).getRoles().contains(Role.BLOCKED)) {
			throw new ServletException("Blocked account.");
		}

		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		// 30 min
		Date endDate = new Date(t + (30 * 60000));
		jwtToken = Jwts.builder().setSubject(email).claim("roles", user.getRoles()).setIssuedAt(new Date())
				.setExpiration(endDate).signWith(SignatureAlgorithm.HS256, KeyFactory.jwtKey).compact();
		KeyFactory.tokenMap.put(user.getId(), jwtToken);
		System.out.println(KeyFactory.tokenMap);
		return jwtToken;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/blockbyid/")
	public void blockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
			throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			int id = u.getId();
			User user = userService.getUserById(id);
			if (user == null) {
				throw new ServletException("No user with that id");
			}
			Collection<Role> roles = user.getRoles();
			roles.add(Role.BLOCKED);
			user.setRoles(roles);
			userService.addUser(user);
			KeyFactory.tokenMap.remove(user.getId());
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/unblockbyid/")
	public void unblockUserbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
			throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			int id = u.getId();
			User user = userService.getUserById(id);
			if (user == null) {
				throw new ServletException("No user with that id");
			}
			Collection<Role> roles = user.getRoles();
			roles.remove(Role.BLOCKED);
			user.setRoles(roles);
			userService.addUser(user);
			KeyFactory.tokenMap.remove(user.getId());
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/resetalltokens/")
	public void resetAllTokens(@RequestHeader(value = "Authorization") String jwt) throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			KeyFactory.tokenMap = new HashMap<Integer, String>();
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/secure/resettokenforid/")
	public void resetTokenbyId(@RequestBody User u, @RequestHeader(value = "Authorization") String jwt)
			throws ServletException {
		if (JwtMyHelper.getIfJWTAdmin(jwt)) {
			int id = u.getId();
			User user = userService.getUserById(id);
			if (user == null) {
				throw new ServletException("No user with that id");
			}
			KeyFactory.tokenMap.remove(id);
		} else {
			throw new ServletException("You are not authorized to do that");
		}

	}

}
