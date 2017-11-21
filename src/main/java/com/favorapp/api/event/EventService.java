package com.favorapp.api.event;

import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.helper.partial_classes.UserPublic;
import com.favorapp.api.user.User;
import com.favorapp.api.user.UserService;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public void save(Event event) {
        eventRepository.save(event);
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }

    public Event getEventByEventId(int id) {
        return eventRepository.getEventById(id);
    }

    public void returnPointsFromExpiredEvents (int userId, UserService userService) {
        eventRepository.getAllByCreatorIdAndEventStateAndLatestStartDateIsBefore(userId, Event_State.TODO, new Date()).forEach(event -> {
            User creator = event.getCreator();
            creator.setPoints(creator.getPoints() + event.getPoints());
            event.setEventState(Event_State.EXPIRED);
            eventRepository.save(event);
            userService.addUser(creator);
        });
    }

    public ArrayList<Event> getAllEvents(double latitude, double longitude) {
        ArrayList<Event> events = new ArrayList<Event>();
        double latVal = 1;
        double longVal = 1;
        double latMax = latitude + latVal;
        double latMin = latitude - latVal;
        double longMax = longitude + longVal;
        double longMin = longitude - longVal;
        events.addAll(eventRepository.getAllByLatitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongitudeGreaterThanEqualAndLongitudeLessThanEqualAndLatestStartDateIsAfterAndStartDateIsNull(latMin, latMax, longMin, longMax, new Date()));
        //eventRepository.findAll().forEach(events::add);
        return events;
    }

    public EventPublic turnEventToEventPublic (Event event) {
        EventPublic eventPublic = new EventPublic();
        BeanUtils.copyProperties(event, eventPublic);
        UserPublic creator = new UserPublic();
        BeanUtils.copyProperties(event.getCreator(), creator);
        eventPublic.setCreator(creator);

        return eventPublic;
    }

    public boolean isUserAParticipantOfEvent(int user_id, int event_id) {
        Event event = getEventByEventId(event_id);
        if (event.getHelper() != null)
            return event.getHelper().getId() == user_id || event.getCreator().getId() == user_id;
        else if(event.getCreator().getId() == user_id) return true;
        return false;
    }
}
