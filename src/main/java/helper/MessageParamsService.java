package helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageParamsService {

    @Autowired
    private MessageParamsRepository messageParamsRepository;


    public String getMessageWithCodes(String messageCode, LanguageCode languageCode) {
        return messageParamsRepository.findMessageParamsByMessageCodeAndLanguageCode(messageCode, languageCode).getMessage();
    }

}
