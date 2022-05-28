package it.os.event.handler.service;

import java.util.List;

import it.os.event.handler.entity.EventETY;

public interface IEventSRV {

    public String save(EventETY event);

    public List<EventETY> getOrderedEvents();

    public boolean insertNewEvent(String eventDescription);

    public void deleteEvent(String eventId);

    public EventETY findById(String eventId);

    public void update(EventETY event);
}
