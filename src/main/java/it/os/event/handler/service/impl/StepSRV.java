package it.os.event.handler.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IStepRepo;
import it.os.event.handler.service.IStepSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StepSRV implements IStepSRV {

    @Autowired
    private IStepRepo stepRepo;

    @Override
    public List<StepETY> generateDefaultSteps(final String eventId) {
        
        final List<StepETY> defaultSteps = new ArrayList<>();
        for (final StepTypeEnum step : StepTypeEnum.values()) {

            final StepETY stepEntity = new StepETY(eventId, step.getName(), step.getDescription());
            defaultSteps.add(stepEntity);
        }
        return defaultSteps;
    }

    @Override
    public String saveStep(final StepETY step) {
        try {
            return stepRepo.save(step);
        } catch (final Exception e) {
            log.error("Error encountered while persisting a newly created step", e);
            throw new BusinessException("Error encountered while persisting a newly created step", e);
        }
    }

    @Override
    public void saveAllSteps(final List<StepETY> steps) {
        
        try {
            stepRepo.saveAll(steps);
        } catch (final Exception e) {
            log.error("Error encountered while persisting a collection of steps", e);
            throw new BusinessException("Error encountered while persisting a collection of steps", e);
        }
        
    }

    @Override
    public List<StepETY> getAllEventSteps(final String eventId) {
        try {
            return stepRepo.getStepsByEventId(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while retrieving all steps for an event", e);
            throw new BusinessException("Error encountered while retrieving all steps for an event", e);
        }
    }

    @Override
    public void deleteAllByEventId(String eventId) {
        
        try {
            stepRepo.deleteAllByEventId(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while deleting all steps for an event", e);
            throw new BusinessException("Error encountered while deleting all steps for an event", e);
        }
    }

    @Override
    public StepETY update(StepETY step) {
        try {
            return stepRepo.update(step);
        } catch (Exception e) {
            log.error(String.format("Error encountered while updating step with Id: %s", step.getId()), e);
            throw new BusinessException(String.format("Error encountered while updating step with Id: %s", step.getId()), e);
        }
    }

    @Override
    public StepETY findById(String stepId) {
        try {
            return stepRepo.findById(stepId);
        } catch (Exception e) {
            log.error(String.format("Error encountered while retrieving step with Id: %s", stepId), e);
            throw new BusinessException(String.format("Error encountered while retrieving step with Id: %s", stepId), e);
        }
    }

}
