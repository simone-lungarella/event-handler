package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.repository.IEventRepo;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;

@SpringBootTest
class EventTest {
    
    @Autowired
    IEventRepo eventRepo;

    @Autowired
    IEventSRV eventSrv;

    @Autowired
    IStepSRV stepSRV;

    @Test
    void persistingTest() {
        final String eventId = eventRepo.save(new EventETY("Test description", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date())));
        assertNotNull(eventId, "The event id should not be null");

        final List<EventETY> list = eventRepo.getAllIncompletedEvents();
        assertTrue(list.stream().map(EventETY::getId).collect(Collectors.toList()).contains(eventId), "The event inserted should be present in the database");
    
        final boolean isPersisted = eventSrv.insertNewEvent("eventDescription");
        assertTrue(isPersisted, "The event should have been persisted");
    }

    @Test
    void deletionTest() {

        // Data preparation
        final boolean isPersisted = eventSrv.insertNewEvent("Event to delete");

        assumeTrue(isPersisted);
        assumeFalse(CollectionUtils.isEmpty(eventSrv.getOrderedEvents()));

        eventSrv.deleteAllEvents();
        assertTrue(CollectionUtils.isEmpty(eventSrv.getOrderedEvents()), "After deletion, no event should be existing");
        assertTrue(CollectionUtils.isEmpty(stepSRV.getAllSteps()), "After deletion, no steps should be existing");
    }

    @Test
    void queryTest() {

        stepSRV.deleteAllSteps();
        eventSrv.deleteAllEvents();
        final boolean isPersisted = eventSrv.insertNewEvent("Event to query");

        assumeTrue(isPersisted);
        assumeFalse(CollectionUtils.isEmpty(eventSrv.getOrderedEvents()));

        final List<EventETY> events = eventSrv.getOrderedEvents();
        final List<StepETY> steps = stepSRV.getAllSteps();
        assertEquals(StepTypeEnum.values().length, steps.size());

        for (StepETY step : steps) {
            assertEquals(step.getEventId(), events.get(0).getId());
        }
    }
}
