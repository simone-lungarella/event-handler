package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.enums.TurbineStateEnum;
import it.os.event.handler.scheduler.RetentionScheduler;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;

/**
 * Test class for retention scheduler.
 * 
 * @author Simone Lungarella
 */
@SpringBootTest(properties = { "event.retention-days=0",
        "spring.datasource.url=jdbc:h2:file:./data/event-handler-test-db" })
class RetentionSchedulerTest {

    @Autowired
    RetentionScheduler retentionScheduler;

    @Autowired
    IEventSRV eventSRV;

    @Autowired
    IStepSRV stepSRV;

    @BeforeEach
    void setup() {
        eventSRV.deleteAllEvents();
    }

    @Test
    void testRun() {

        // Data preparation
        final boolean isInserted = eventSRV.insertNewEvent("Turbine name", "eventDescription",
                OperationTypeEnum.GENERATOR_REPLACING, TurbineStateEnum.LIMITED,
                LocalDate.now(), LocalDate.now());

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
}
