package com.favorapp.api.event;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface EventRequestRepository extends CrudRepository<EventRequest, Long> {

    Collection<EventRequest> findAllByEventId (int id);

    Collection<EventRequest> findAllByUserId(int id);
}
