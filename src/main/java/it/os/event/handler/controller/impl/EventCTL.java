package it.os.event.handler.controller.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import it.os.event.handler.controller.IEventCTL;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.exception.StepNotFoundException;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EventCTL implements IEventCTL {

    @Autowired
    private IEventSRV eventSRV;

    @Autowired
    private IStepSRV stepSRV;

    @Override
    public ResponseEntity<String> createEvent (final EventRequest requestBody, final HttpServletRequest request) {

        log.info("Creation a new event with description: {}", requestBody.getDescription());
        
        final boolean isPersisted = eventSRV.insertNewEvent(requestBody.getTurbineName(), requestBody.getDescription(), 
            requestBody.getOperation(), requestBody.getTurbineState(), requestBody.getStartingDateEEMM(), requestBody.getStartingDateOOCC());

        if (isPersisted) {
            return new ResponseEntity<>("Event persisted correctly", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error while persisting event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<EventETY>> getAllEvents(final HttpServletRequest request) {
        log.info("Retrieving all events");
        
        final List<EventETY> events = eventSRV.getOrderedEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StepETY>> getStepsByEventId(final String eventId, final HttpServletRequest request) {

        log.info("Retrieving all steps for event with id: {}", eventId);
        
        final List<StepETY> steps = stepSRV.getAllEventSteps(eventId);
        return new ResponseEntity<>(steps, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(String eventId, HttpServletRequest request) {
       
        log.info("Deleting event with id: {}", eventId);
        
        eventSRV.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> setStepCompletion(final String stepId, final Boolean isComplete, HttpServletRequest request) {
        
        final StepETY step = stepSRV.findById(stepId);
        if (step != null) {
            step.setComplete(isComplete);
            stepSRV.update(step);
            eventSRV.updateEventStep(step);
        } else {
            throw new StepNotFoundException("Step");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StepETY>> getSteps(HttpServletRequest request) {
        log.info("Retrieving all steps");
        
        final List<StepETY> steps = stepSRV.getAllSteps();
        return new ResponseEntity<>(steps, HttpStatus.OK);
    }

}
