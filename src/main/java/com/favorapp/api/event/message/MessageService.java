package com.favorapp.api.event.message;

import com.favorapp.api.helper.partial_classes.MessagePrivate;
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

    public MessagePrivate turnMessageToMessagePrivate(Message msg) {
        MessagePrivate messagePrivate = new MessagePrivate();
        messagePrivate.setContent(msg.getContent());
        messagePrivate.setCreationDate(msg.getCreationDate());
        messagePrivate.setEvent_id(msg.getEvent().getId());
        messagePrivate.setId(msg.getId());
        messagePrivate.setSender_id(msg.getSender().getId());
        messagePrivate.setType(msg.getType());
        return messagePrivate;
    }


}
