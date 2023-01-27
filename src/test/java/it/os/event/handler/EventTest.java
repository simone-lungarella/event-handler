package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.enums.TurbinePower;
import it.os.event.handler.enums.TurbineStateEnum;
import it.os.event.handler.repository.IEventRepo;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EventTest extends AbstractTest {

    @Autowired
    IEventRepo eventRepo;

    @Autowired
    IEventSRV eventSrv;

    @Autowired
    IStepSRV stepSRV;

    @BeforeEach
    void setup() {
        eventRepo.deleteAll();
        stepSRV.deleteAllSteps();
    }

    @Test
    @DisplayName("Test event creation")
    void whenEventIsInserted_shouldBePersisted() {
        final List<String> operations = Arrays.asList(OperationTypeEnum.SOST_GENERATORE.getDescription());
        final Integer eventId = eventRepo.save(new EventETY("Turbine name", "XXXX", "Test description",
                TurbinePower.MEGAWATT.getName(), operations, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()), TurbineStateEnum.MARCHING.getName()));

        assertNotNull(eventId, "The event id should not be null");

        final List<EventETY> list = eventRepo.getAllEvents();
        assertTrue(list.stream().map(EventETY::getId).collect(Collectors.toList()).contains(eventId),
                "The event inserted should be present in the database");

        final boolean isPersisted = eventSrv.insertNewEvent(getRandomEventRequest());
        assertTrue(isPersisted, "The event should have been persisted");
    }

    @Test
    @DisplayName("Test event deletion")
    void whenEventsAreDeleted_shouldBeRemovedFromDb() {

        // Data preparation
        final boolean isPersisted = eventSrv.insertNewEvent(getRandomEventRequest());

        assumeTrue(isPersisted);
        assumeFalse(CollectionUtils.isEmpty(eventSrv.getOrderedEvents(true)));

        eventSrv.deleteAllEvents();
        assertTrue(CollectionUtils.isEmpty(eventSrv.getOrderedEvents(true)), "After deletion, no event should be existing");
        assertTrue(CollectionUtils.isEmpty(stepSRV.getAllSteps()), "After deletion, no steps should be existing");
    }

    @Test
    @DisplayName("Test step creation")
    void whenEventIsCreated_stepsShouldBeInserted() {

        final boolean isPersisted = eventSrv.insertNewEvent(getRandomEventRequest());

        assumeTrue(isPersisted);
        assumeFalse(CollectionUtils.isEmpty(eventSrv.getOrderedEvents(true)));

        final List<EventETY> events = eventSrv.getOrderedEvents(true);
        final List<StepETY> steps = stepSRV.getAllSteps();
        assertEquals(StepTypeEnum.values().length, steps.size());

        for (StepETY step : steps) {
            assertEquals(step.getEventId(), events.get(0).getId());
        }
    }

    @Test
    @DisplayName("Test step update")
    void whenStepIsUpdated_databaseShouldBeFullyUpdated() {
        final boolean isPersisted = eventSrv.insertNewEvent(getRandomEventRequest());

        assumeTrue(isPersisted);
        assumeFalse(CollectionUtils.isEmpty(eventSrv.getOrderedEvents(true)));

        final List<EventETY> allEvents = eventSrv.getOrderedEvents(true);
        final EventETY vipEvent = allEvents.get(0);

        assumeFalse(vipEvent == null);

        final List<StepETY> allEventSteps = stepSRV.getAllEventSteps(vipEvent.getId());
        assertDoesNotThrow(() -> eventSrv.updateEventStep(allEventSteps.get(0)));

        final EventETY updatedVip = eventSrv.findById(vipEvent.getId());
        assertNull(updatedVip.getCompletionDate());

        assertDoesNotThrow(() -> eventSrv.updateEventStep(allEventSteps.get(allEventSteps.size() - 2)));

        final EventETY updatedCompleteVip = eventSrv.findById(vipEvent.getId());
        assertNull(updatedCompleteVip.getCompletionDate());

        assertDoesNotThrow(() -> eventSrv.updateEventStep(allEventSteps.get(allEventSteps.size() - 1)));
    }

}
