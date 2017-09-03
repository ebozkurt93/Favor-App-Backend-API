package com.favorapp.api.helper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.internal.Nullable;

import javax.validation.constraints.Null;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONResponse<T> {
    @JsonFormat
    private boolean success;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_time")
    private Date createdTime;
    @JsonFormat
    @Nullable
    private T payload;
    @JsonFormat
    @Nullable
    private JSONResponseError error;

    public JSONResponse() {
        this.createdTime = new Date();
    }

    public JSONResponse(boolean success, T payload) {
        this.success = success;
        this.payload = payload;
        this.createdTime = new Date();
    }

    public JSONResponse(boolean success, T payload, JSONResponseError error) {
        this.success = success;
        this.payload = payload;
        this.error = error;
        this.createdTime = new Date();
    }


    public JSONResponse(MessageParamsService messageParamsService) {
        this.createdTime = new Date();
        this.messageParamsService = messageParamsService;
    }

    private MessageParamsService messageParamsService;


    private static final int count = 0;
    private static final int maxTries = 5;

    public JSONResponse errorDefault(String error) {

        try {
            return new JSONResponse<Null>(false, null, new JSONResponseError(messageParamsService.getMessageValue(error, LanguageCode.en)));
        } catch (Exception ex) {
            JSONResponseError err = new JSONResponseError("Unknown Error");
            return new JSONResponse<Null>(false, null, err);
        }
    }

    public JSONResponse errorDefault(int error_code, String error) {
        try {
            return new JSONResponse<Null>(false, null, new JSONResponseError(error_code, messageParamsService.getMessageValue(error, LanguageCode.en)));
        } catch (Exception ex) {
            JSONResponseError err = new JSONResponseError("Unknown Error");
            return new JSONResponse<Null>(false, null, err);
        }
    }

    public static JSONResponse<Null> successNoPayloadDefault() {

        int c = count;

        while (true) {
            try {
                return new JSONResponse<Null>(true, (Null) null);
            } catch (Exception ex) {
                if (++c == maxTries) throw ex;
            }
        }
    }

    public JSONResponse<T> successWithPayloadDefault(T payload) {
        int c = count;

        while (true) {
            try {
                return new JSONResponse<T>(true, payload);
            } catch (Exception ex) {
                if (++c == maxTries) throw ex;
            }
        }
    }

}
