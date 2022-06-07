package it.os.event.handler.repository;

import java.util.List;

import it.os.event.handler.entity.EventETY;

/**
 * Interface of event repository.
 * 
 * @author Simone Lungarella
 */
public interface IEventRepo {

    /**
     * Persist a new event.
     * 
     * @param event Event to persist.
     * @return Persisted event identifier.
     */
    Integer save(EventETY entity);

    /**
     * Returns the list of incomplete events ordered by completion percentage.
     * 
     * @return List of incomplete events.
     */
    List<EventETY> getAllEvents();

    /**
     * Delete the event identified by its {@code eventId} and all its steps.
     * 
     * @param eventId Event identifier.
     */
    void deleteById(Integer eventId);

    /**
     * Returns the event identified by its {@code eventId}.
     * 
     * @param eventId Event identifier.
     * @return Event identified by its {@code eventId}.
     */
    EventETY findById(Integer eventId);

    /**
     * Update an event.
     * 
     * @param event Event to update.
     */
    void update(EventETY event);

    /**
     * Return all completed events existing in the repository.
     * 
     * @return List of completed events.
     */
    List<EventETY> getAllCompletedEvents();

    /**
     * Delete all events existing.
     */
    void deleteAll();

}
