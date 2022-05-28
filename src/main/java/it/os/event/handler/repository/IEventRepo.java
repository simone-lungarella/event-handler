package it.os.event.handler.repository;

import java.util.List;

import it.os.event.handler.entity.EventETY;

public interface IEventRepo {
    
    String save(EventETY entity);

    List<EventETY> getAllIncompletedEvents();

    void deleteById(String eventId);

    EventETY findById(String eventId);

    void update(EventETY event);

}
