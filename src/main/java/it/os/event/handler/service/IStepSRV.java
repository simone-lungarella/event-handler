package it.os.event.handler.service;

import java.util.List;

import it.os.event.handler.entity.StepETY;

public interface IStepSRV {

    /**
     * Generates all the initial steps of a new event.
     * 
     * @param eventId Event identifier to assign to each step.
     * @return List of steps generated.
     */
    List<StepETY> generateDefaultSteps(String eventId);

    /**
     * Persist a new step.
     * 
     * @param step New step to persist.
     * @return Persisted step identifier.
     */
    Integer saveStep(StepETY step); 

    /**
     * Persists a list of steps.
     * 
     * @param steps Steps to insert.
     */
    void saveAllSteps(List<StepETY> steps);

    /**
     * Returns steps associated to an event identified by its {@code eventId}.
     * 
     * @param eventId Event identifier.
     * @return Step associated to the event.
     */
    List<StepETY> getAllEventSteps(String eventId);

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
     * Return all step existing.
     * 
     * @return All step existing.
     */
    List<StepETY> getAllSteps();

    /**
     * Delete all existing steps.
     */
    void deleteAllSteps();
}
