package com.favorapp.api.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.favorapp.api.event.message.Message;
import com.favorapp.api.user.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    @ManyToOne
    @JoinColumn(name = "helper_id")
    private User helper;
    @Column(nullable = false, length = 100)
    private String description;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Event_Category category;
    @Column(nullable = false, precision = 16, scale = 14)
    private double latitude;
    @Column(nullable = false, precision = 17, scale = 14)
    private double longitude;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "latest_start_date", nullable = false)
    private Date latestStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "start_date")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "event_state", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Event_State eventState;

    @JsonManagedReference
    @OneToMany(cascade = javax.persistence.CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    private Collection<EventRequest> eventRequests;

    private int creatorRating;
    private int helperRating;

    //todo message
    //@JsonManagedReference
    @OneToMany(cascade = javax.persistence.CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    Collection<Message> messages;


    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getHelper() {
        return helper;
    }

    public void setHelper(User helper) {
        this.helper = helper;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Event_Category getCategory() {
        return category;
    }

    public void setCategory(Event_Category category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLatestStartDate() {
        return latestStartDate;
    }

    public void setLatestStartDate(Date latestStartDate) {
        this.latestStartDate = latestStartDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Event_State getEventState() {
        return eventState;
    }

    public void setEventState(Event_State eventState) {
        this.eventState = eventState;
    }

    public Collection<EventRequest> getEventRequests() {
        return eventRequests;
    }

    public void setEventRequests(Collection<EventRequest> eventRequests) {
        this.eventRequests = eventRequests;
    }

    public int getCreatorRating() {
        return creatorRating;
    }

    public void setCreatorRating(int creatorRating) {
        this.creatorRating = creatorRating;
    }

    public int getHelperRating() {
        return helperRating;
    }

    public void setHelperRating(int helperRating) {
        this.helperRating = helperRating;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }
}
