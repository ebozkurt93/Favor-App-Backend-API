package com.favorapp.api.event;

import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.helper.partial_classes.UserPublic;
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

    public Event getEventById(int id) {
        return eventRepository.getEventById(id);
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

}
