package it.os.event.handler.repository;

import java.util.List;

import it.os.event.handler.entity.StepETY;

public interface IStepRepo {

    String save(StepETY step);

    void saveAll(List<StepETY> steps);

    List<StepETY> getStepsByEventId(String eventId);

    void deleteAllByEventId(String eventId);

    StepETY update(StepETY step);

    StepETY findById(String stepId);
}
