package com.favorapp.api.demo;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.event.*;
import com.favorapp.api.helper.*;
import com.favorapp.api.helper.log.LogService;
import com.favorapp.api.helper.partial_classes.client_wrappers.EventRequestAccept;
import com.favorapp.api.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    @RequestMapping(method = RequestMethod.POST, value = "/secure/accepteventrequest")
    public JSONResponse acceptEventRequest(@RequestHeader(value = "Authorization") String jwt, @RequestBody EventRequestAccept eventRequestAccept) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        User requestingUser = userService.getUserById(eventRequestAccept.getUser().getId());
        Event event = eventService.getEventById(eventRequestAccept.getEvent().getId());
        if(event == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (requestingUser == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
        if (!eventRequestService.isThereSuchRequest(event.getId(), requestingUser.getId()))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_REQUEST);
        if(!(event.getEventState() == Event_State.TODO && event.getLatestStartDate().after(new Date())))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EVENT_EXPIRED);

        //todo accept request, open chat between both sides, change event state 
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test")
    public JSONResponse sendRequest() {
        return new JSONResponse().successWithPayloadDefault(eventService.getEventById(13));
    }

}
