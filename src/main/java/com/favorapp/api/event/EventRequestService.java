package com.favorapp.api.event;

import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.helper.partial_classes.EventRequestPrivate;
import com.favorapp.api.helper.partial_classes.UserPublic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EventRequestService {

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Autowired
    private EventService eventService;

    public void save(EventRequest eventRequest) {
        eventRequestRepository.save(eventRequest);
    }

    public Collection<EventRequest> getAllRequestsByEventId(int id) {
        return eventRequestRepository.findAllByEventId(id);
    }

    public Collection<EventRequest> getAllRequestsByUserId(int id) {
        return eventRequestRepository.findAllByUserId(id);
    }

    public EventRequestPrivate turnEventRequestToEventRequestPrivate(EventRequest request) {
        EventRequestPrivate eventRequestPrivate = new EventRequestPrivate();
        BeanUtils.copyProperties(request, eventRequestPrivate);
        EventPublic eventPublic = eventService.turnEventToEventPublic(request.getEvent());
        UserPublic userPublic = new UserPublic();
        BeanUtils.copyProperties(request.getUser(), userPublic);
        eventRequestPrivate.setEvent(eventPublic);
        eventRequestPrivate.setUser(userPublic);

        /*UserPublic creator = new UserPublic();
        BeanUtils.copyProperties(event.getCreator(), creator);
        eventRequestPrivate.setCreator(creator);*/
        return eventRequestPrivate;
    }
}
