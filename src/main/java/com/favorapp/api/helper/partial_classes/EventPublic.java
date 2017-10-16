package com.favorapp.api.helper.partial_classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.favorapp.api.event.Event_Category;
import com.favorapp.api.user.User;

import java.util.Date;

public class EventPublic {

    private int id;
    private UserPublic creator;
    private UserPublic helper;
    private String description;
    private int points;
    private Event_Category category;
    private double latitude;
    private double longitude;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;

    public EventPublic() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserPublic getCreator() {
        return creator;
    }

    public void setCreator(UserPublic creator) {
        this.creator = creator;
    }

    public UserPublic getHelper() {
        return helper;
    }

    public void setHelper(UserPublic helper) {
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
    
}
