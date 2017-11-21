package com.favorapp.api.demo;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.event.*;
import com.favorapp.api.event.message.Message;
import com.favorapp.api.event.message.MessageService;
import com.favorapp.api.event.message.MessageType;
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

    @Autowired
    private MessageService messageService;


    @RequestMapping(method = RequestMethod.POST, value = "/secure/getallmessagesfromevent")
    public JSONResponse getAllMessagesFromEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody Event event) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        event = eventService.getEventByEventId(event.getId());
        if (event == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (!eventService.isUserAParticipantOfEvent(user.getId(), event.getId()))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        return new JSONResponse().successWithPayloadDefault(messageService.getMessagesByEventId(event.getId()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getmessagecountfromevent")
    public JSONResponse getMessageCountFromEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody Event event) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        event = eventService.getEventByEventId(event.getId());
        if (event == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (!eventService.isUserAParticipantOfEvent(user.getId(), event.getId()))
            return new JSONResponse(messageParamsService).errorDefault("1");
        return new JSONResponse().successWithPayloadDefault(messageService.getMessageCountForEvent(event.getId()));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/getpointsbackfromoldevents")
    public JSONResponse getPointsOfExpiredEvent(@RequestHeader(value = "Authorization") String jwt) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        eventService.returnPointsFromExpiredEvents(user.getId(), userService);
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/accepteventrequest")
    public JSONResponse acceptEventRequest(@RequestHeader(value = "Authorization") String jwt, @RequestBody EventRequestAccept eventRequestAccept) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        User requestingUser = userService.getUserById(eventRequestAccept.getUser().getId());
        Event event = eventService.getEventByEventId(eventRequestAccept.getEvent().getId());
        if (event == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (requestingUser == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_USER_WITH_ID);
        if (!eventRequestService.isThereSuchRequest(event.getId(), requestingUser.getId()))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_REQUEST);
        if (!(event.getEventState() == Event_State.TODO && event.getLatestStartDate().after(new Date())))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EVENT_EXPIRED);

        event.setEventState(Event_State.IN_PROGRESS);
        eventService.save(event);
        //todo somehow inform other side
        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/secure/sendmessage")
    public JSONResponse sendMessage(@RequestHeader(value = "Authorization") String jwt, @RequestBody Message message) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        Event event = eventService.getEventByEventId(message.getEvent().getId());
        if (event == null)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (!eventService.isUserAParticipantOfEvent(user.getId(), event.getId()))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        if (event.getEventState() != Event_State.IN_PROGRESS)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EVENT_EXPIRED);

        if (message.getType() == MessageType.TEXT && message.getContent() == null || message.getContent().trim().length() == 0)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_MESSAGE);
        if (message.getType() == MessageType.EVENT_END_APPROVAL && !messageService.checkEventEndRequestExists(event.getId()))
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.INVALID_MESSAGE);
        if (message.getType() == MessageType.EVENT_END_APPROVAL) {
            event.setEventState(Event_State.IN_REVIEW);
            event.setEndDate(new Date());
        }
        eventService.save(event);

        message.setSender(user);
        message.setCreationDate(new Date());
        message.setEvent(event);
        messageService.save(message);

        return JSONResponse.successNoPayloadDefault();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test")
    public JSONResponse sendRequest() {
        return new JSONResponse().successWithPayloadDefault(eventService.getEventByEventId(13));
    }

}
