package com.favorapp.api.demo;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.config.key.KeyFactory;
import com.favorapp.api.config.security.PasswordEncoder;
import com.favorapp.api.event.*;
import com.favorapp.api.helper.*;
import com.favorapp.api.helper.log.LogService;
import com.favorapp.api.helper.partial_classes.EventCreate;
import com.favorapp.api.helper.partial_classes.EventRequestPrivate;
import com.favorapp.api.helper.partial_classes.UserEditProfile;
import com.favorapp.api.helper.partial_classes.UserMyAccount;
import com.favorapp.api.user.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private EventRequestService eventRequestService;


    @RequestMapping(method = RequestMethod.POST, value = "/secure/getmyeventrequests")
    public JSONResponse sendRequest(@RequestHeader(value = "Authorization") String jwt) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        Collection<EventRequest> requests = eventRequestService.getAllRequestsByUserId(user.getId());
        Collection<EventRequestPrivate> eventRequestPrivates = new ArrayList<>();
        requests.forEach(request -> {
            eventRequestPrivates.add(eventRequestService.turnEventRequestToEventRequestPrivate(request));
        });
        return new JSONResponse().successWithPayloadDefault(eventRequestPrivates);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test")
    public JSONResponse sendRequest() {
        return new JSONResponse().successWithPayloadDefault(eventService.getEventById(13));
    }

}
