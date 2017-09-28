package com.favorapp.api.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.favorapp.api.user.User;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "request_date", nullable = false)
    private DateTime requestDate;

    public EventRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }
}
