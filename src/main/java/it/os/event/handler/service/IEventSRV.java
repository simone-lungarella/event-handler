package it.os.event.handler.service;

import java.util.List;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;

/**
 * Interface service of event handler.
 * 
 * @author Simone Lungarella
 */
public interface IEventSRV {

    /**
     * Persist a new event.
     * 
     * @param event Event to persist.
     * @return Persisted event identifier.
     */
    public String save(EventETY event);

    /**
     * Returns the list of incomplete events ordered by completion percentage.
     * 
     * @return List of incomplete events.
     */
    public List<EventETY> getOrderedEvents();

    /**
     * Execute the insertion of a new event and its steps.
     * 
     * @param eventDescription Event description.
     * @return {@code true} if the event is inserted correctly, {@code false}
     *         otherwise.
     */
    public boolean insertNewEvent(String eventDescription);

    /**
     * Delete and event identified by its {@code eventId} and all its steps.
     * 
     * @param eventId Event identifier.
     */
    public void deleteEvent(String eventId);

    /**
     * Returns the event identified by its {@code eventId}.
     * 
     * @param eventId Event identifier.
     * @return Event identified by its {@code eventId}.
     */
    public EventETY findById(String eventId);

    /**
     * Update an event.
     * 
     * @param event Event to update.
     */
    public void update(EventETY event);

    /**
     * Updates a step associated to an event and refresh the same event.
     * 
     * @param step Step to update.
     */
    public void updateEventStep(StepETY step);

    /**
     * Returns all the completed events still existing and archived.
     * 
     * @return List of completed events.
     */
    public List<EventETY> getAllCompletedEvents();

    /**
     * Delete all events existing.
     */
    public void deleteAllEvents();
}
