package it.os.event.handler.controller.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import it.os.event.handler.controller.IEventCTL;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.exception.EventNotFoundException;
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
    public ResponseEntity<String> createEvent(final EventRequest requestBody, final HttpServletRequest request) {

        log.info("Creating windfarm of name: {}", requestBody.getTurbineName());

        final boolean isPersisted = eventSRV.insertNewEvent(requestBody);

        if (isPersisted) {
            return new ResponseEntity<>("Event persisted correctly", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error while persisting event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateEvent(final EventRequest requestBody, final HttpServletRequest request) {

        log.info("Update windfarm with name: {}", requestBody.getTurbineName());
        final List<EventETY> events = eventSRV.getOrderedEvents(true).stream()
                .filter(event -> event.getTurbineName().equals(requestBody.getTurbineName()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(events)) {
            throw new EventNotFoundException("Event with name: " + requestBody.getTurbineName() + " not found");
        } else {
            final EventETY event = events.get(0);
            event.setDescription(requestBody.getDescription());
            event.setOdlNumber(requestBody.getOdlNumber());
            event.setOperation(requestBody.getOperation());
            event.setTurbineState(requestBody.getTurbineState());
            event.setTurbineNumber(requestBody.getTurbineNumber());
            event.setPower(requestBody.getPower());
            event.setToNotDismantle(requestBody.getToNotDismantle() != null ? requestBody.getToNotDismantle() : false);

            if (requestBody.getStartingDateEEMM() != null) {
                event.setStartingDateEEMM(requestBody.getStartingDateEEMM().toString());
            }
            if (requestBody.getStartingDateOOCC() != null) {
                event.setStartingDateOOCC(requestBody.getStartingDateOOCC().toString());
            }
            if (requestBody.getPriorNotification() != null) {
                event.setPriorNotification(requestBody.getPriorNotification().toString());
            }
            if (requestBody.getPermittingDate() != null) {
                event.setPermittingDate(requestBody.getPermittingDate().toString());
            }

            eventSRV.update(event);

            return new ResponseEntity<>("Event updated correctly", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<EventETY>> getAllEvents(boolean includeCompleted, final HttpServletRequest request) {
        log.info("Retrieving all events");

        final List<EventETY> events = eventSRV.getOrderedEvents(includeCompleted);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StepETY>> getStepsByEventId(final Integer eventId, final HttpServletRequest request) {

        log.info("Retrieving all steps for event with id: {}", eventId);

        final List<StepETY> steps = stepSRV.getAllEventSteps(eventId);
        return new ResponseEntity<>(steps, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEvent(Integer eventId, HttpServletRequest request) {

        log.info("Deleting event with id: {}", eventId);

        eventSRV.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> setStepCompletion(final Integer stepId, final Boolean isComplete,
            HttpServletRequest request) {

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

    @Override
    public ResponseEntity<byte[]> getAllEventsAsCsv(HttpServletRequest request) {

        final byte[] csvFile = eventSRV.getTurbinesForExport();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("text/csv"));
        httpHeaders.set( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv" );

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("text/csv"))
                .headers(httpHeaders)
                .body(csvFile);
    }

    @Override
    public ResponseEntity<List<EventETY>> getAllCompletedEvents(HttpServletRequest request) {
        log.info("Retrieving all completed events");

        final List<EventETY> events = eventSRV.getAllCompletedEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

}
