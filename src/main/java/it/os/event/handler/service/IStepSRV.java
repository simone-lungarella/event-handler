package it.os.event.handler.service;

import java.util.List;

import it.os.event.handler.entity.StepETY;

public interface IStepSRV {

    List<StepETY> generateDefaultSteps(String eventId);

    String saveStep(StepETY step);

    void saveAllSteps(List<StepETY> steps);

    List<StepETY> getAllEventSteps(String eventId);

    void deleteAllByEventId(String eventId);

    StepETY update(StepETY step);

    StepETY findById(String stepId);
}
