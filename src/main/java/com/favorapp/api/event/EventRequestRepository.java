package com.favorapp.api.event;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

public interface EventRequestRepository extends CrudRepository<EventRequest, Long> {

    Collection<EventRequest> findAllByEventCreatorIdAndEventLatestStartDateAfterAndEventEventState(int id, Date latestStartDate, Event_State eventState);

    EventRequest getByUserIdAndEventId(int userId, int EventId);

    EventRequest getById(int requestId);

}
