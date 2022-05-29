package it.os.event.handler.repository;

import java.util.List;

import it.os.event.handler.entity.StepETY;

/**
 * Interface of step repository.
 * 
 * @author Simone Lungarella
 */
public interface IStepRepo {

    /**
     * Persist a new step.
     * 
     * @param step Step to persist.
     * @return Persisted step identifier.
     */
    String save(StepETY step);

    /**
     * Persists a list of steps.
     * 
     * @param step Steps to insert.
     */
    void saveAll(List<StepETY> steps);

    /**
     * Returns steps associated to an event identified by its {@code eventId}.
     * 
     * @param stepId Event identifier.
     * @return Step associated to the event.
     */
    List<StepETY> getStepsByEventId(String eventId);

    /**
     * Deletes all steps associated to an event identified by its {@code eventId}.
     * 
     * @param eventId Event identifier.
     */
    void deleteAllByEventId(String eventId);

    /**
     * Update a step.
     * 
     * @param stepId Step to update.
     * @return Updated step.
     */
    StepETY update(StepETY step);

    /**
     * Returns the step identified by its {@code stepId}.
     * 
     * @param stepId Identifier of the step to get.
     * @return Step identified by its {@code stepId}.
     */
    StepETY findById(String stepId);

    /**
     * Returns all existing steps.
     * 
     * @return All existing steps.
     */
    List<StepETY> findAll();

    /**
     * Delete all existing stesp.
     */
    void deleteAll();
    
}
