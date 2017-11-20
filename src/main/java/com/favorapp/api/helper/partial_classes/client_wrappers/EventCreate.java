package com.favorapp.api.helper.partial_classes.client_wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.favorapp.api.event.Event;

public class EventCreate {

    //keep this json property otherwise it'll assume name "is_now" so wont find value, therefore will assume value is always false
    @JsonProperty("isNow")
    private boolean isNow;
    private Event event;

    public EventCreate() {
    }

    public boolean isNow() {
        return isNow;
    }

    public void setNow(boolean now) {
        isNow = now;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventCreate{" +
                "isNow=" + isNow +
                ", event=" + event +
                '}';
    }
}
