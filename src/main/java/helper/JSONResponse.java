package helper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DateTimeDV;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JSONResponse<T> {
    @JsonFormat
    private boolean success;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date created_time;
    @JsonFormat
    @Nullable
    private T payload;
    @JsonFormat
    @Nullable
    private JSONResponseError error;

    public JSONResponse() {
        this.created_time = new Date();
    }

    private JSONResponse(boolean success, T payload) {
        this.success = success;
        this.payload = payload;
        this.created_time = new Date();
    }

    private JSONResponse(boolean success, T payload, JSONResponseError error) {
        this.success = success;
        this.payload = payload;
        this.error = error;
        this.created_time = new Date();
    }


    private static final int count = 0;
    private static final int maxTries = 5;

    public static JSONResponse errorDefault(MessageCode error) {
        try {
            return new JSONResponse<Null>(false, null, new JSONResponseError(new MessageParamsService().getMessageWithCodes(error, LanguageCode.en)));
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
