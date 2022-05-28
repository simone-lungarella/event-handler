package it.os.event.handler.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
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
    public String save(EventETY entity) {

        try {
            return eventRepo.save(entity);
        } catch (Exception e) {
            log.error("Error encountered while persisting entity.", e);
            throw new BusinessException("Error encountered while persisting entity.", e);
        }

    }

    @Override
    public List<EventETY> getOrderedEvents() {
        
        List<EventETY> orderedEvents = new ArrayList<>();
        try {
            final List<EventETY> events = eventRepo.getAllIncompletedEvents();

            if (!CollectionUtils.isEmpty(events)) {
                // TODO: Implement logic to order events
            }
            
        } catch (Exception e) {
            log.error("Error encountered while retrieving ordered events.", e);
            throw new BusinessException("Error encountered while retrieving ordered events.", e);
        }

        return orderedEvents;
    }

    @Override
    public boolean insertNewEvent(String eventDescription) {

        boolean isSuccessful = false;
        try {

            EventETY event = new EventETY(eventDescription, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
            final String eventId = eventRepo.save(event);

            final List<StepETY> steps = stepSrv.generateDefaultSteps(eventId);
            stepSrv.saveAllSteps(steps);

            isSuccessful = true;
        } catch (Exception e) {
            log.error("Error encountered while persisting a new complete event.", e);
            throw new BusinessException("Error encountered while persisting a new complete event.", e);
        }

        return isSuccessful;
    }

    @Override
    public void deleteEvent(String eventId) {

        try {
            eventRepo.deleteById(eventId);
            stepSrv.deleteAllByEventId(eventId);
        } catch (Exception e) {
            log.error("Error encountered while deleting an event.", e);
            throw new BusinessException("Error encountered while deleting an event.", e);
        }
    }

    @Override
    public EventETY findById(String eventId) {
        try {
            return eventRepo.findById(eventId);
        } catch (Exception e) {
            log.error("Error encountered while retrieving an event.", e);
            throw new BusinessException("Error encountered while retrieving an event.", e);
        }
    }

    @Override
    public void update(EventETY event) {
        
        try {
            eventRepo.update(event);
        } catch (Exception e) {
            log.error("Error encountered while updating an event.", e);
            throw new BusinessException("Error encountered while updating an event.", e);
        }
        
    }
}
