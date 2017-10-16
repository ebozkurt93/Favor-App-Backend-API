package com.favorapp.api.event;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
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
        event.setCreationDate(new Date());
        System.out.println(event.getCreationDate().toString());
        eventRepository.save(event);
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }
//todo add control for getting non active events only, events which still are doable as time etc
    public ArrayList<Event> getAllEvents(double latitude, double longitude) {
        ArrayList<Event> events = new ArrayList<Event>();
        double latVal = 1;
        double longVal = 1;
        double latMax = latitude + latVal;
        double latMin = latitude - latVal;
        double longMax = longitude + longVal;
        double longMin = longitude - longVal;
        eventRepository.getAllByLatitudeGreaterThanAndLatitudeLessThanAndLongitudeGreaterThanAndLongitudeLessThan(latMax, latMin, longMax, longMin).forEach(events::add);
        return events;
    }

}
