package helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageCodeService {

    @Autowired
    private MessageCodeRepository messageCodeRepository;

    private MessageCode getMessageCode(int id){
        return messageCodeRepository.findMessageCodeByValue(id);
    }
}
