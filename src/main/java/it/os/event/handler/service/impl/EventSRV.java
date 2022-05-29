package it.os.event.handler.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IEventRepo;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventSRV implements IEventSRV {

    @Autowired
    private IEventRepo eventRepo;

    @Autowired
    private IStepSRV stepSrv;

    @Override
    public String save(final EventETY entity) {

        try {
            return eventRepo.save(entity);
        } catch (final Exception e) {
            log.error("Error encountered while persisting entity.", e);
            throw new BusinessException("Error encountered while persisting entity.", e);
        }

    }

    @Override
    public List<EventETY> getOrderedEvents() {

        final List<EventETY> orderedEvents = new ArrayList<>();
        try {
            final List<EventETY> events = eventRepo.getAllIncompletedEvents();

            log.info("Ordering events by their completion percentage");
            if (!CollectionUtils.isEmpty(events)) {
                for (int i = 0; i <= StepTypeEnum.values().length; i++) {
                    final int currentStep = i;

                    log.info("Ordering same percentage events by their creation date");
                    final List<EventETY> samePercentage = events.stream()
                            .filter(ev -> currentStep == ev.getCompletedSteps()).collect(Collectors.toList());
                    samePercentage.sort((ev1, ev2) -> ev1.getCreationDate().compareTo(ev2.getCreationDate()));
                    orderedEvents.addAll(samePercentage);
                }
            }
        } catch (final Exception e) {
            log.error("Error encountered while retrieving ordered events.", e);
            throw new BusinessException("Error encountered while retrieving ordered events.", e);
        }

        return orderedEvents;
    }

    @Override
    public boolean insertNewEvent(final String eventDescription) {

        boolean isSuccessful = false;
        try {

            final EventETY event = new EventETY(eventDescription,
                    new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
            final String eventId = eventRepo.save(event);

            final List<StepETY> steps = stepSrv.generateDefaultSteps(eventId);
            stepSrv.saveAllSteps(steps);

            isSuccessful = true;
        } catch (final Exception e) {
            log.error("Error encountered while persisting a new complete event.", e);
            throw new BusinessException("Error encountered while persisting a new complete event.", e);
        }

        return isSuccessful;
    }

    @Override
    public void deleteEvent(final String eventId) {

        try {
            eventRepo.deleteById(eventId);
            stepSrv.deleteAllByEventId(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while deleting an event.", e);
            throw new BusinessException("Error encountered while deleting an event.", e);
        }
    }

    @Override
    public EventETY findById(final String eventId) {
        try {
            return eventRepo.findById(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while retrieving an event.", e);
            throw new BusinessException("Error encountered while retrieving an event.", e);
        }
    }

    @Override
    public void update(final EventETY event) {

        try {
            eventRepo.update(event);
        } catch (final Exception e) {
            log.error("Error encountered while updating an event.", e);
            throw new BusinessException("Error encountered while updating an event.", e);
        }

    }

    @Override
    public void updateEventStep(final StepETY step) {
        try {
            final EventETY event = findById(step.getEventId());
            event.setCompletedSteps(event.getCompletedSteps() + 1);

            if (StepTypeEnum.CHIUSURA_CANTIERE.equals(StepTypeEnum.get(step.getName()))) {
                event.setComplete(true);
                event.setCompletionDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
            }
            update(event);
        } catch (final Exception e) {
            log.error("Error encountered while updating an event step.", e);
            throw new BusinessException("Error encountered while updating an event step.", e);
        }
    }

    @Override
    public List<EventETY> getAllCompletedEvents() {

        try {
            return eventRepo.getAllCompletedEvents();
        } catch (Exception e) {
            log.error("Error encountered while retrieving completed events.", e);
            throw new BusinessException("Error encountered while retrieving completed events.", e);
        }
    }

    @Override
    public void deleteAllEvents() {

        try {
            eventRepo.deleteAll();
            stepSrv.deleteAllSteps();
        } catch (Exception e) {
            log.error("Error encountered while deleting all events.", e);
            throw new BusinessException("Error encountered while deleting all events.", e);
        }
    }
}