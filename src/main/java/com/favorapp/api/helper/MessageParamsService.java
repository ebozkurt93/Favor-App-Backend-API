package com.favorapp.api.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageParamsService {

    @Autowired
    MessageParamsRepository messageParamsRepository;

    public String getMessageValue(String key, LanguageCode languageCode) {
        return messageParamsRepository.findByKeyAndLanguageCode(key, languageCode).getValue();
    }
}
