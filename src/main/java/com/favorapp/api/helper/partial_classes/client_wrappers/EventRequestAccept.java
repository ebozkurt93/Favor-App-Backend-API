package com.favorapp.api.helper.partial_classes.client_wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventRequestAccept {

    @JsonProperty("request_id")
    private int requestId;

    public EventRequestAccept() {
    }

    public int getRequestId() {
        return requestId;
    }
}
