package com.favorapp.api.event;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    Event getEventById(int id);

    ArrayList<Event> getAllByLatitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongitudeGreaterThanEqualAndLongitudeLessThanEqual(double latMax, double latMin, double longMax, double longMin);

    ArrayList<Event> getAllByLatitudeGreaterThanEqualAndLatitudeLessThanEqualAndLongitudeGreaterThanEqualAndLongitudeLessThanEqualAndLatestStartDateIsAfterAndStartDateIsNull(double latMax, double latMin, double longMax, double longMin, Date latestStartDate);

}
