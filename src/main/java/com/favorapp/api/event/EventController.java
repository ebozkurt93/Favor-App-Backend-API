package com.favorapp.api.event;

import com.favorapp.api.config.JwtMyHelper;
import com.favorapp.api.helper.JSONResponse;
import com.favorapp.api.helper.MessageCode;
import com.favorapp.api.helper.MessageParamsService;
import com.favorapp.api.helper.partial_classes.EventCreate;
import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.helper.partial_classes.UserPublic;
import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
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

    @RequestMapping(method = RequestMethod.POST, value = "/secure/createevent")
    public JSONResponse createEvent(@RequestHeader(value = "Authorization") String jwt, @RequestBody EventCreate eventCreate) {
        User user = new JwtMyHelper(userService).getUserFromJWT(jwt);
        Event event = eventCreate.getEvent();
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

    @RequestMapping(method = RequestMethod.GET, value = "/getallevents/{latitude}/{longitude}")
    public JSONResponse getAllEventsLatLong(@RequestHeader(value = "Authorization") String jwt, @PathVariable double latitude, @PathVariable double longitude) {
        //todo add the use of jwt here, at least check if it is valid
        return new JSONResponse<ArrayList<Event>>().successWithPayloadDefault(eventService.getAllEvents(latitude, longitude));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getallevents")
    public JSONResponse getAllEvents() {
        //todo add the use of jwt here, at least check if it is valid
        List<EventPublic> events = new ArrayList<>();
        eventService.getAllEvents().forEach(event -> {
            EventPublic eventPublic = new EventPublic();
            BeanUtils.copyProperties(event, eventPublic);
            UserPublic creator = new UserPublic();
            BeanUtils.copyProperties(event.getCreator(), creator);
            eventPublic.setCreator(creator);

            events.add(eventPublic);
        });

        //eventService.getAllEvents().forEach(event -> BeanUtils.copyProperties(event, UserPublic u = new UserPublic()));
        return new JSONResponse<>().successWithPayloadDefault(events);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sendrequest")
    public JSONResponse sendRequest(@RequestHeader(value = "Authorization") String jwt) {
        User user = new JwtMyHelper().getUserFromJWT(jwt);
        if (user.getActiveEventCount() < 3)/*also if user didnt send request to this event before*/ {
            //todo something
        }


        return new JSONResponse<ArrayList<Event>>().successWithPayloadDefault(eventService.getAllEvents());
    }
}
