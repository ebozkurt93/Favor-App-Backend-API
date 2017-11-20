package com.favorapp.api.event;

import com.favorapp.api.helper.partial_classes.EventPublic;
import com.favorapp.api.helper.partial_classes.EventRequestPrivate;
import com.favorapp.api.helper.partial_classes.UserPublic;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class EventRequestService {

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Autowired
    private EventService eventService;

    public void save(EventRequest eventRequest) {
        eventRequestRepository.save(eventRequest);
    }



    public Collection<EventRequest> getAllRequestsToUsersEventsByUserId(int id) {
        return eventRequestRepository.findAllByEventCreatorIdAndEventLatestStartDateAfterAndEventEventState(id, new Date(), Event_State.TODO);
    }

    public boolean isThereSuchRequest(int eventId, int userId){
        if(eventRequestRepository.getByUserIdAndEventId(userId, eventId) != null) return true;
        else return false;
    }

    public EventRequestPrivate turnEventRequestToEventRequestPrivate(EventRequest request) {
        EventRequestPrivate eventRequestPrivate = new EventRequestPrivate();
        BeanUtils.copyProperties(request, eventRequestPrivate);
        EventPublic eventPublic = eventService.turnEventToEventPublic(request.getEvent());
        UserPublic userPublic = new UserPublic();
        BeanUtils.copyProperties(request.getUser(), userPublic);
        eventRequestPrivate.setEvent(eventPublic);
        eventRequestPrivate.setUser(userPublic);
        return eventRequestPrivate;
    }
}
