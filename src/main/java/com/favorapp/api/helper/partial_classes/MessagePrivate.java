package com.favorapp.api.helper.partial_classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.favorapp.api.event.message.MessageType;

import java.util.Date;

public class MessagePrivate {

    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;
    private int sender_id;
    private int event_id;
    private MessageType type;
    private String content;

    public MessagePrivate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
