package com.favorapp.api.demo;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.key.KeyFactory;
import com.favorapp.api.config.security.PasswordEncoder;
import com.favorapp.api.event.Event;
import com.favorapp.api.event.EventController;
import com.favorapp.api.event.EventService;
import com.favorapp.api.event.Event_State;
import com.favorapp.api.helper.*;
import com.favorapp.api.helper.log.LogService;
import com.favorapp.api.helper.partial_classes.EventCreate;
import com.favorapp.api.helper.partial_classes.UserEditProfile;
import com.favorapp.api.user.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/demo/")
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageParamsService messageParamsService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserRolesService userRolesService;

    @Autowired
    private EventService eventService;


    @RequestMapping(method = RequestMethod.POST, value = "/editprofile")
    public JSONResponse editProfile(@RequestHeader(value = "Authorization") String jwt, UserEditProfile updatedInfo){
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        if(updatedInfo.getName() != null) {
            user.setName(updatedInfo.getName());
        }
        if (updatedInfo.getLastname() != null) {
            user.setLastname(updatedInfo.getLastname());
        }
        //todo add description
        /*if (updatedInfo.getDescription() != null) {

        }*/
        if (updatedInfo.getEmail() != null && updatedInfo.getCurrentPassword() != null) {
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            if (!passwordEncoder.matches(updatedInfo.getCurrentPassword(), user.getPassword())) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.WRONG_PASSWORD);
            }
            else {
                //todo send an email to user before and after email address change
                user.setEmail(updatedInfo.getEmail());
            }
        }
        if(updatedInfo.getNewPassword() != null && updatedInfo.getCurrentPassword() != null) {
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            if (!passwordEncoder.matches(updatedInfo.getCurrentPassword(), user.getPassword())) {
                return new JSONResponse(messageParamsService).errorDefault(MessageCode.WRONG_PASSWORD);
            }
            String newPassword = passwordEncoder.encode(updatedInfo.getNewPassword());
            user.setPassword(newPassword);
            //todo send an email to user
        }

        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public JSONResponse login(@RequestBody User login) {

        String jwtToken = "";
        if (login.getEmail() == null || login.getPassword() == null) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.FILL_USERNAME_PASSWORD);
        }

        String email = login.getEmail();
        String password = login.getPassword();

        User user = userService.getUserByEmail(email);

        if (user == null) {
            //sending invalid login because we don't want to give details to user
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_LOGIN);
        }

        String encodedPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        if (!passwordEncoder.matches(password, encodedPassword)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_LOGIN);
        }

        if (userService.getUserByEmail(email).getRoles().contains(Role.BLOCKED)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.BLOCKED_ACCOUNT);
        }

        if (userService.getUserByEmail(email).getRoles().contains(Role.VALIDATE_EMAIL)) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EMAIL_NOT_VALIDATED);
        }

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        // 30 min
        //todo enable this back
        //Date endDate = new Date(t + (30 * 60000));
        Date endDate = new Date(t + (10000 * 30 * 60000));

        jwtToken = Jwts.builder().setSubject(email).claim("roles", user.getRoles()).setIssuedAt(new Date())
                .setExpiration(endDate).signWith(SignatureAlgorithm.HS256, KeyFactory.jwtKey).compact();
        KeyFactory.tokenMap.put(user.getId(), jwtToken);
        System.out.println(KeyFactory.tokenMap);
        return new JSONResponse<String>().successWithPayloadDefault(jwtToken);

    }

    @RequestMapping(value = "test")
    public void test() {
        User u = userService.getUserById(4);
        /*
        Collection<UserRoles> roles = u.getRoles();
        roles.add(new UserRoles(u, Role.ADMIN));
        u.setRoles(roles);*/
        userRolesService.save(new UserRoles(u, Role.ADMIN));
        //roles.forEach(userRoles -> System.out.println(userRoles.getId() + " " + userRoles.getRole() + " " + userRoles.getUser().getId()));
        //userService.addUser(u);
        //return ResponseEntity.ok(JSONResponse.successNoPayloadDefault());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tst")
    public void createEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody EventCreate eventCreate) {
        EventController eventController = new EventController();

        eventController.createEvent(jwt, eventCreate);
    }


    public JSONResponse createEventBackup(@RequestHeader(value = "Authorization") String jwt, @RequestBody Event event) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        event.setEventState(Event_State.TODO);
        event.setCreator(user);

        Long t = Calendar.getInstance().getTimeInMillis();
        event.setLatestStartDate(new Date(t + (60000 * 60 * 1)));
        if (user.getPoints() < event.getPoints() || event.getPoints() == 0) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NOT_ENOUGH_POINTS);
        }
        if (user.getActiveEventCount() >= 3) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.ACTIVE_EVENT_COUNT);
        }
        eventService.save(event);
        user.setPoints(user.getPoints() - event.getPoints());
        user.setActiveEventCount(user.getActiveEventCount() + 1);
        userService.addUser(user);
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(value = "test2")
    public void test2() {
        //try gson
        userRolesService.getAllUserRoles().forEach(userRoles -> System.out.println(userRoles.getId() + " " + userRoles.getRole() + " " + userRoles.getUser().getId()));
        //return messageParamsService.getMessageValue(MessageCode.ERROR, LanguageCode.en);
    }
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
