package com.favorapp.api.user;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.MailSenderService;
import com.favorapp.api.config.key.KeyFactory;

import io.jsonwebtoken.Claims;
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
		return "Hi";

	}

	//todo remove/edit this
	@RequestMapping(method = RequestMethod.POST, value = "/emaildemo")
	public void emailDemo() throws MailException, InterruptedException {
		User user = new User();
		user.setEmail("favorapp2017@gmail.com");

		mailSenderService.sendEmail(user);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/email_validation/{p1}/{p2}/{p3}")
	public void validateEmail(@PathVariable String p1, @PathVariable String p2, @PathVariable String p3)
			throws ServletException {

		String token = p1 + "." + p2 + "." + p3;
		System.out.println(token);
		Claims claims = Jwts.parser().setSigningKey(KeyFactory.jwtKey).parseClaimsJws(token).getBody();
		System.out.println(claims);

		String email = (String) claims.get("email_validation");
		User user = userService.getUserByEmail(email);
		Collection<Role> roles = user.getRoles();
		if (!roles.contains(Role.VALIDATE_EMAIL)) {
			throw new ServletException("This account is already validated.");
		}
		roles.remove(Role.VALIDATE_EMAIL);

		user.setRoles(roles);
		userService.addUser(user);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/register")
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
			// TODO enable this later
			// u.getRoles().add(Role.VALIDATE_EMAIL);

			userService.addUser(u);

			// get email validation token
			Calendar dateNow = Calendar.getInstance();
			long t = dateNow.getTimeInMillis();
			// 1 day
			Date endDate = new Date(t + (1 * 60 * 60000));
			String jwtToken = Jwts.builder().claim("email_validation", email).setIssuedAt(new Date())
					.setExpiration(endDate).signWith(SignatureAlgorithm.HS256, KeyFactory.jwtKey).compact();
			System.out.println(jwtToken);
			// send email to token

			String[] tokenParts = jwtToken.split("\\.");
			System.out.println(tokenParts.length);
			int hash = email.hashCode();
			String finalLink = "http://localhost:8080/user/email_validation/" + tokenParts[0] + "/" + tokenParts[1]
					+ "/" + tokenParts[2]; // continue from here
			String emailaddress = "favorapp2017@gmail.com";
			String subject = "Welcome to Boon";
			String text = "link is " + finalLink;
			System.out.println(text);

			/*
			 * TODO enable this to send emails try {
			 * mailSenderService.sendEmailWithDetails(emailaddress, subject,
			 * text); } catch (MailException | InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */
		} else
			throw new ServletException("The email address is already in use");
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

		if (userService.getUserByEmail(email).getRoles().contains(Role.VALIDATE_EMAIL)) {
			throw new ServletException("Please validate your email address.");
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

}
