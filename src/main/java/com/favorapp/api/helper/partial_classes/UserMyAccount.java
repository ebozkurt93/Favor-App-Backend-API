package com.favorapp.api.helper.partial_classes;

public class UserMyAccount {

    private int id;
    private String name;
    private String lastname;
    private String email;
    private int points;
    private int activeEventCount;
    private double rating;

    public UserMyAccount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getActiveEventCount() {
        return activeEventCount;
    }

    public void setActiveEventCount(int activeEventCount) {
        this.activeEventCount = activeEventCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
