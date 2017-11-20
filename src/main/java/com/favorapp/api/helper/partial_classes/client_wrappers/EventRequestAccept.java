package com.favorapp.api.helper.partial_classes.client_wrappers;

import com.favorapp.api.event.Event;
import com.favorapp.api.user.User;

public class EventRequestAccept {

    private Event event;
    private User user;

    public EventRequestAccept() {
    }

    public Event getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }
}
