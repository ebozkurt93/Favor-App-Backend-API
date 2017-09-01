package com.favorapp.api.helper;

public final class MessageCode {

    //region MessageParams Commonly Sent To Client
    public static final String ERROR = "ERROR";
    public static final String ALREADY_VALIDATED_ACCOUNT = "ALREADY_VALIDATED_ACCOUNT";
    public static final String EMAIL_ALREADY_IN_USE = "EMAIL_ALREADY_IN_USE";
    //could be id password doesn't match or something else, don't give information to user about it
    public static final String INVALID_LOGIN = "INVALID_LOGIN";
    public static final String BLOCKED_ACCOUNT = "BLOCKED_ACCOUNT";
    public static final String EMAIL_NOT_VALIDATED = "EMAIL_NOT_VALIDATED";
    //endregion

    //region Other(Internal) MessageParams
    public static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";
    public static final String NO_USER_WITH_ID = "NO_USER_WITH_ID";
    public static final String NO_USER_WITH_EMAIL = "NO_USER_WITH_EMAIL";
    public static final String FILL_USERNAME_PASSWORD = "FILL_USERNAME_PASSWORD";
    public static final String EMAIL_ADDRESS_DOESNT_FIT_TO_REGEX = "EMAIL_ADDRESS_DOESNT_FIT_TO_REGEX";
    //endregion

}