package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.config.SchedulerCFG;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.scheduler.NotificationScheduler;
import it.os.event.handler.scheduler.RetentionScheduler;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IMailSRV;
import it.os.event.handler.service.IStepSRV;

/**
 * Test class for retention scheduler.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SchedulerTest extends AbstractTest {

    @Autowired
    RetentionScheduler retentionScheduler;

    @Autowired
    NotificationScheduler notificationScheduler;

    @Autowired
    IEventSRV eventSRV;

    @Autowired
    IStepSRV stepSRV;

    @MockBean
    IMailSRV mailSRV;

    @BeforeEach
    void setup() {
        eventSRV.deleteAllEvents();
        given(mailSRV.sendMail(anyString(), anyString(), anyBoolean())).willReturn(true);
    }

    @Test
    @DisplayName("Test retention scheduler")
    void whenRetentionIsExecuted_allExpiredEventShouldBeDeleted() {
        // Data preparation
        final boolean isInserted = eventSRV.insertNewEvent(getRandomEventRequest());

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
        final List<String> operations = Arrays.asList(OperationTypeEnum.SOST_GENERATORE.getDescription());

        final boolean isInserted = eventSRV.insertNewEvent(getEventForScheduler(turbineName, operations, LocalDate.now()));
        
        assumeTrue(isInserted, "The event should be inserted to test the scheduler");

        List<EventETY> events = eventSRV.getOrderedEvents();
        EventETY expiredEvent = events.stream().filter(event -> event.getTurbineName().equals(turbineName)).collect(Collectors.toList()).get(0);
        assumeFalse(expiredEvent.isMailSent(), "Mail should not have been sent yet");

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
        final List<String> operations = Arrays.asList(OperationTypeEnum.SOST_GENERATORE.getDescription());

        final boolean isInserted = eventSRV.insertNewEvent(getEventForScheduler(turbineName, operations, null));
        
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
