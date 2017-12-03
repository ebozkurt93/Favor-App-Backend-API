package com.favorapp.api.event;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.helper.JSONResponse;
import com.favorapp.api.helper.MessageCode;
import com.favorapp.api.helper.MessageParamsService;
import com.favorapp.api.helper.partial_classes.client_wrappers.EventCreate;
import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/event/")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageParamsService messageParamsService;

    @Autowired
    private EventRequestService eventRequestService;

    @RequestMapping(method = RequestMethod.POST, value = "/secure/createevent")
    public JSONResponse createEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody EventCreate eventCreate) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        Event event = eventCreate.getEvent();
        event.setCreationDate(new Date());
        event.setEventState(Event_State.TODO);
        event.setCreator(user);
        if (eventCreate.isNow()) {
            Long t = Calendar.getInstance().getTimeInMillis();
            //1 hour
            event.setLatestStartDate(new Date(t + (60000 * 60 * 1)));
        } else {
            Long t = Calendar.getInstance().getTimeInMillis();
            //24 hours
            event.setLatestStartDate(new Date(t + (60000 * 60 * 24)));
        }

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

    @RequestMapping(method = RequestMethod.GET, value = "/secure/getallevents/{latitude}/{longitude}")
    public JSONResponse getAllEventsLatLong(@RequestHeader(value = "Authorization") String jwt, @PathVariable double latitude, @PathVariable double longitude) {
        List<EventPublic> events = new ArrayList<>();
        eventService.getAllEvents(latitude, longitude).forEach(event -> events.add(eventService.turnEventToEventPublic(event)));
        return new JSONResponse<>().successWithPayloadDefault(events);
    }

    /*
    @RequestMapping(method = RequestMethod.GET, value = "/secure/getallevents")
    public JSONResponse getAllEvents(@RequestHeader(value = "Authorization") String jwt) {
        List<EventPublic> events = new ArrayList<>();
        eventService.getAllEvents().forEach(event -> events.add(eventService.turnEventToEventPublic(event)));
        return new JSONResponse<>().successWithPayloadDefault(events);
    }
    */

    @RequestMapping(method = RequestMethod.POST, value = "/secure/sendrequest")
    public JSONResponse sendRequest(@RequestHeader(value = "Authorization") String jwt, @RequestBody Event clientEvent) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        if (user.getActiveEventCount() >= 3) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.ACTIVE_EVENT_COUNT);
        }
        Event event = eventService.getEventByEventId(clientEvent.getId());
        if (event == null) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.NO_EVENT_WITH_ID);
        } else if (event.getLatestStartDate().before(new Date()) || event.getEventState() != Event_State.TODO)
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.EVENT_EXPIRED);
        if (event.getCreator().getId() == user.getId()) {
            return new JSONResponse(messageParamsService).errorDefault(MessageCode.OWN_EVENT);
        }
        boolean alreadySentRequest = false;
        for (EventRequest er : event.getEventRequests()) {
            if (er.getUser() == user) {
                alreadySentRequest = true;
            }
            if (alreadySentRequest)
                return new JSONResponse(messageParamsService).errorDefault(1, MessageCode.ALREADY_SENT_REQUEST);
        }

        EventRequest eventRequest = new EventRequest(event, user, new Date());
        eventRequestService.save(eventRequest);

        return JSONResponse.successNoPayloadDefault();
    }
}
