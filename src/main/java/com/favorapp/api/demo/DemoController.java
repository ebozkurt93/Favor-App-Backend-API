package com.favorapp.api.demo;

import com.fasterxml.jackson.annotation.*;
import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.event.Event;
import com.favorapp.api.event.EventController;
import com.favorapp.api.event.EventService;
import com.favorapp.api.event.Event_State;
import com.favorapp.api.helper.*;
import com.favorapp.api.helper.log.Log;
import com.favorapp.api.helper.log.LogRepository;
import com.favorapp.api.helper.log.LogService;
import com.favorapp.api.helper.partial_classes.EventCreate;
import com.favorapp.api.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
