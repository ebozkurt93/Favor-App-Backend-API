package com.favorapp.api.event.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void save(Message message) {
        messageRepository.save(message);
    }

    public Collection<Message> getMessagesByEventId(int id) {
        //todo check here
        return messageRepository.findAllByEventIdOrderByCreationDateDesc(id);
    }

    public boolean checkEventEndRequestExists(int event_id) {
        Message msg = messageRepository.findByEventIdAndType(event_id, MessageType.EVENT_END_REQUEST);
        if (msg == null) return false;
        else return true;
    }

    public int getMessageCountForEvent(int eventId) {
        return messageRepository.countMessagesByEventId(eventId);
    }


}
