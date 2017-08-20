package helper;

public enum MessageCode {
    already_validated_account(0),
    not_valid_email(1),
    email_already_in_use(2),
    not_authorized(3),
    no_user_with_id(4),
    no_user_with_email(5),
    invalid_login(6),
    blocked_account(7),
    fill_username_password(8);


    private final int value;

    MessageCode(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

}
