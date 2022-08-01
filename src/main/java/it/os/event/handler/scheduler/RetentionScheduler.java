package it.os.event.handler.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.service.IEventSRV;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler that execute retention of events.
 */
@Slf4j
@Component
public class RetentionScheduler {

    @Value("${event.retention-days}")
    private Integer retentionDays;

    @Autowired
    private IEventSRV eventSRV;

    @Scheduled(cron = "${event.retention-schedule}")
    public void run() {

        log.info("Retrieving all completed events");

        try {
            final Date retentionDate = getRetentionThreshold();
            final List<EventETY> events = eventSRV.getAllCompletedEvents();

            if (!CollectionUtils.isEmpty(events)) {
                for (EventETY event : events) {
                    if (new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(event.getCompletionDate())
                            .before(retentionDate)) {
                        log.info("Deleting event with id: {}", event.getId());
                        eventSRV.deleteEvent(event.getId());
                    } else {
                        log.info("Event with id: {} is not eligible for deletion", event.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while executing retention scheduler");
        }

        log.info("Retention scheduler complete");
    }

    private Date getRetentionThreshold() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, retentionDays);
        return calendar.getTime();
    }

}
