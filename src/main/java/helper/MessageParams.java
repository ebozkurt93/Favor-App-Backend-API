package helper;


import org.hibernate.annotations.Table;

import javax.persistence.*;
@Entity(name = "message_params")
public class MessageParams {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "message_code", nullable = false)
    private MessageCode messageCode;
    @Column(name = "message_language_code", nullable = false)
    private LanguageCode messageLanguageCode;
    @Column(name = "message", nullable = false)
    private String message;

    public MessageParams() {
    }
/*
    public MessageParams(MessageCode messageCode, LanguageCode messageLanguageCode, String message) {
        this.messageCode = messageCode;
        this.messageLanguageCode = messageLanguageCode;
        this.message = message;
    }
*/
    public MessageParams(MessageCode messageCode, String message) {
        messageLanguageCode = LanguageCode.en;
        this.messageCode = messageCode;
        this.message = message;
    }

    public MessageCode getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(MessageCode messageCode) {
        this.messageCode = messageCode;
    }

    public LanguageCode getMessageLanguageCode() {
        return messageLanguageCode;
    }

    public void setMessageLanguageCode(LanguageCode messageLanguageCode) {
        this.messageLanguageCode = messageLanguageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
