package com.favorapp.api.helper.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.favorapp.api.user.User;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.UUID;

@Entity(name = "log_master")
public class Log {

    @Id
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "event_details", nullable = false)
    private String eventDetails;

    private Log() {
    }

    public Log(User user, String eventDetails) {
        this.id = UUID.randomUUID().toString();
        this.createdTime = new Date();
        this.user = user;
        this.eventDetails = eventDetails;
    }

    public Log(String eventDetails) {
        this.id = UUID.randomUUID().toString();
        this.createdTime = new Date();
        this.eventDetails = eventDetails;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
}
