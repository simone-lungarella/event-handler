package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import com.mysql.cj.protocol.Protocol.ProtocolEventListener.EventType;

import it.os.event.handler.config.SchedulerCFG;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.enums.TurbinePower;
import it.os.event.handler.enums.TurbineStateEnum;
import it.os.event.handler.scheduler.NotificationScheduler;
import it.os.event.handler.scheduler.RetentionScheduler;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;

/**
 * Test class for retention scheduler.
 */
@SpringBootTest(properties = { 
    "spring.datasource.url=jdbc:postgresql://localHost:5432/event_handler", 
    "spring.datasource.username=postgres",
    "spring.datasource.password=admin", })
@ActiveProfiles("test")
class SchedulerTest {

    @Autowired
    RetentionScheduler retentionScheduler;

    @Autowired
    NotificationScheduler notificationScheduler;

    @Autowired
    IEventSRV eventSRV;

    @Autowired
    IStepSRV stepSRV;

    @BeforeEach
    void setup() {
        eventSRV.deleteAllEvents();
    }

    @Test
    @DisplayName("Test retention scheduler")
    void whenRetentionIsExecuted_allExpiredEventShouldBeDeleted() {

        final List<String> operations = Arrays.asList(OperationTypeEnum.GENERATOR_REPLACING.getDescription());

        // Data preparation
        final boolean isInserted = eventSRV.insertNewEvent("Turbine name", "XXXX", "eventDescription", TurbinePower.MEGAWATT.getName(),
                operations, TurbineStateEnum.LIMITED, LocalDate.now(), LocalDate.now(), "1");

        assumeTrue(isInserted, "The event should be inserted to test the scheduler");

        final EventETY insertedEvent = eventSRV.getOrderedEvents().get(0);
        final List<StepETY> steps = stepSRV.getAllEventSteps(insertedEvent.getId());
        assumeFalse(CollectionUtils.isEmpty(steps), "All steps must be existing and set to complete to test retention");

        for (StepETY step : steps) {
            StepETY updatedStep = new StepETY(step.getEventId(), step.getName(), step.getDescription());
            updatedStep.setComplete(true);
            stepSRV.update(updatedStep);
        }

        // Asserting well executed retention
        assertDoesNotThrow(() -> retentionScheduler.run());
        assertTrue(CollectionUtils.isEmpty(eventSRV.getAllCompletedEvents()));
    }

    @MockBean
    SchedulerCFG schedulerCFG;

    @Test
    @DisplayName("Test notification scheduler")
    void whenEventIsExpired_notificationMustBeSent() {

        final String turbineName = "RO01";
        final List<String> operations = Arrays.asList(OperationTypeEnum.GENERATOR_REPLACING.getDescription());

        final boolean isInserted = eventSRV.insertNewEvent(turbineName, "1982", "Test", TurbinePower.MEGAWATT.getName(), 
            operations, TurbineStateEnum.LIMITED, LocalDate.now().minusDays(1), LocalDate.now(), "1");
        
        assumeTrue(isInserted, "The event should be inserted to test the scheduler");

        List<EventETY> events = eventSRV.getOrderedEvents();
        EventETY expiredEvent = events.stream().filter(event -> event.getTurbineName().equals(turbineName)).collect(Collectors.toList()).get(0);
        assumeFalse(expiredEvent.isMailSent(), "Mail should not have been sent yet");

        // Updating permitting date
        final List<StepETY> steps = stepSRV.getAllEventSteps(expiredEvent.getId());
        for (int i=1; i<=StepTypeEnum.PERMITTING.getOrder(); i++) {
            steps.get(i).setComplete(true);
            stepSRV.update(steps.get(i));
            eventSRV.updateEventStep(steps.get(i));
        }
        
        given(schedulerCFG.getMegaWThreshold()).willReturn(0);
        assertDoesNotThrow(() -> notificationScheduler.run());

        events = eventSRV.getOrderedEvents();
        expiredEvent = events.stream().filter(event -> event.getTurbineName().equals(turbineName)).collect(Collectors.toList()).get(0);
        assertTrue(expiredEvent.isMailSent(), "Mail should have been sent");
    }

    @Test
    @DisplayName("Test notification scheduler with no expired events")
    void givenNonExpiredEvent_shouldNotSendNotification() {
        
        final String turbineName = "Test turbine";
        final List<String> operations = Arrays.asList(OperationTypeEnum.GENERATOR_REPLACING.getDescription());

        final boolean isInserted = eventSRV.insertNewEvent(turbineName, "XXXX", "Test", 
            TurbinePower.MEGAWATT.getName(), operations, TurbineStateEnum.LIMITED, LocalDate.now(), LocalDate.now(), "1");
        
        assumeTrue(isInserted, "The event should be inserted to test the scheduler");

        List<EventETY> events = eventSRV.getOrderedEvents();
        EventETY expiredEvent = events.stream().filter(event -> event.getTurbineName().equals(turbineName)).collect(Collectors.toList()).get(0);
        assumeFalse(expiredEvent.isMailSent(), "Mail should not have been sent yet");

        given(schedulerCFG.getMegaWThreshold()).willReturn(5);
        assertDoesNotThrow(() -> notificationScheduler.run());

        events = eventSRV.getOrderedEvents();
        expiredEvent = events.stream().filter(event -> event.getTurbineName().equals(turbineName)).collect(Collectors.toList()).get(0);
        assertFalse(expiredEvent.isMailSent(), "Mail should not have been sent if expiration is not reached");
    }
}
