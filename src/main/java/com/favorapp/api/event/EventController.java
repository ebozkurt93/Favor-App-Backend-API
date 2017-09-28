package com.favorapp.api.event;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.helper.JSONResponse;
import com.favorapp.api.helper.MessageCode;
import com.favorapp.api.helper.MessageParams;
import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/event/")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/secure/createevent")
    public JSONResponse createEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody Event event) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        event.setEventState(Event_State.TODO);
        event.setCreator(user);
        if(user.getPoints() < event.getPoints()) {
            return new JSONResponse().errorDefault(MessageCode.NOT_ENOUGH_POINTS);
        }
        eventService.save(event);
        user.setPoints(user.getPoints() - event.getPoints());
        userService.addUser(user);
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getallevents")
    public JSONResponse getAllEvents() {
        return new JSONResponse().successWithPayloadDefault(eventService.getAllEvents());
    }
}
