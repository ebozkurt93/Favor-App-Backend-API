package com.favorapp.api.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.internal.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONResponseError {
    @Nullable
    private int error_code = -1;
    @Nullable
    private String message;

    public JSONResponseError() {
    }

    public JSONResponseError(int error_code, String message) {
        this.error_code = error_code;
        this.message = message;
    }

    @JsonIgnoreProperties()
    public JSONResponseError(String message) {
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
