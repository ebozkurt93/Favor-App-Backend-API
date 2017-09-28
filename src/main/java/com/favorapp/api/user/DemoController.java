package com.favorapp.api.user;

import com.fasterxml.jackson.annotation.*;
import com.favorapp.api.helper.*;
import com.favorapp.api.helper.log.Log;
import com.favorapp.api.helper.log.LogRepository;
import com.favorapp.api.helper.log.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @RequestMapping(value = "test1")
    public Collection<UserRoles> test1() {
        return userService.getUserById(4).getRoles();
        //return messageParamsService.getMessageValue(MessageCode.ERROR, LanguageCode.en);
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
