package com.favorapp.api.helper;

import javax.persistence.*;

@Entity(name = "message_params")
public class MessageParams {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "key", nullable = false)
    private String key;
    @Column(name = "language_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    @Column(name = "value", nullable = false)
    private String value;

    public MessageParams(String key, String messageValue) {
        this.key = key;
        this.languageCode = LanguageCode.en;
        this.value = messageValue;
    }

    public MessageParams() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
