package com.favorapp.api.helper.partial_classes;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class EventRequestPrivate {

    private int id;
    private EventPublic event;
    private UserPublic user;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date requestDate;

    public EventRequestPrivate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventPublic getEvent() {
        return event;
    }

    public void setEvent(EventPublic event) {
        this.event = event;
    }

    public UserPublic getUser() {
        return user;
    }

    public void setUser(UserPublic user) {
        this.user = user;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
