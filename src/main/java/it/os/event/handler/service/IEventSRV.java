package it.os.event.handler.service;

import java.util.List;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.StepTypeEnum;

/**
 * Interface service of event handler.
 */
public interface IEventSRV {

    /**
     * Returns the list of incomplete events ordered by completion percentage.
     * 
     * @return List of incomplete events.
     */
    public List<EventETY> getOrderedEvents();

    /**
     * Returns all steps that have reached a specific step.
     * 
     * @param reachedStep The step that has been reached.
     * @return List of steps that have reached the specified step.
     */
    public List<EventETY> getEvents(StepTypeEnum reachedStep);

    /**
     * Execute the insertion of a new event and its steps.
     * 
     * @param requestBody Request body containing all event informations.
     * @return {@code true} if the event is inserted correctly, {@code false} otherwise.
     */
    public boolean insertNewEvent(EventRequest requestBody);

    /**
     * Delete and event identified by its {@code eventId} and all its steps.
     * 
     * @param eventId Event identifier.
     */
    public void deleteEvent(Integer eventId);

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

    /**
     * Construct a csv file with information about all events.
     * 
     * @return Resource with the csv file.
     */
    public byte[] getTurbinesForExport();

    /**
     * Set to {@code true} the flag {@code isMailSent} of the event identified by its {@code eventId}.
     * 
     * @param id Identifier of event.
     */
    public void setMailSent(Integer id);

    /**
     * Returns all the uncompleted events.
     * 
     * @return List of uncompleted events.
     */
    public List<EventETY> getUncompletedEvents();
}
