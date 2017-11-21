package com.favorapp.api.event.message;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Collection<Message> findAllByEventIdOrderByCreationDateDesc(int id);

    Message findByEventIdAndType(int id, MessageType type);

    int countMessagesByEventId(int id);
}
