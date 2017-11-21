package com.favorapp.api.helper;

public final class MessageCode {

    //region MessageParams Commonly Sent To Client
    public static final String ERROR = "ERROR";
    //for example user tries to validate account by clicking mail link over and over
    public static final String ALREADY_VALIDATED_ACCOUNT = "ALREADY_VALIDATED_ACCOUNT";
    public static final String EMAIL_ALREADY_IN_USE = "EMAIL_ALREADY_IN_USE";
    //could be id password doesn't match or something else, don't give information to user about it
    public static final String INVALID_LOGIN = "INVALID_LOGIN";
    public static final String BLOCKED_ACCOUNT = "BLOCKED_ACCOUNT";
    public static final String EMAIL_NOT_VALIDATED = "EMAIL_NOT_VALIDATED";
    //already doing too many events, therefore can't create/do any other
    public static final String ACTIVE_EVENT_COUNT = "ACTIVE_EVENT_COUNT";
    //used in cases like changing profile information such as email address, password
    public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
    //already sent request to that event
    public static final String ALREADY_SENT_REQUEST = "ALREADY_SENT_REQUEST";
    public static final String EVENT_EXPIRED = "EVENT_EXPIRED";
    //event is created by you, so you cannot send request
    public static final String OWN_EVENT = "OWN_EVENT";


    //endregion

    //region Other(Internal) MessageParams
    public static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";
    public static final String NO_USER_WITH_ID = "NO_USER_WITH_ID";
    public static final String NO_USER_WITH_EMAIL = "NO_USER_WITH_EMAIL";
    public static final String FILL_USERNAME_PASSWORD = "FILL_USERNAME_PASSWORD";
    public static final String EMAIL_ADDRESS_DOESNT_FIT_TO_REGEX = "EMAIL_ADDRESS_DOESNT_FIT_TO_REGEX";
    public static final String NOT_ENOUGH_POINTS = "NOT_ENOUGH_POINTS";
    public static final String NO_EVENT_WITH_ID = "NO_EVENT_WITH_ID";
    //there is no such event request, user is trying to do event with someone who didn't actually request it
    public static final String NO_EVENT_REQUEST = "NO_EVENT_REQUEST";
    //event is not suitable for sending a message, it is not in progress state
    public static final String EVENT_NOT_SUITABLE = "EVENT_NOT_SUITABLE";
    //message may be empty
    public static final String INVALID_MESSAGE = "INVALID_MESSAGE";


    //endregion

}
