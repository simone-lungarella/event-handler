package it.os.event.handler.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.config.SchedulerCFG;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.enums.TurbinePower;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IMailSRV;
import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler that sends notifications.
 */
@Slf4j
@Component
public class NotificationScheduler {

    @Autowired
    private SchedulerCFG schedulerCFG;

    @Autowired
    private IEventSRV eventSRV;

    @Autowired
    private IMailSRV mailSRV;

    @Scheduled(cron = "${event.notification-schedule}")
    public void run() {

        log.info("Retrieving all completed events");

        try {
            final List<EventETY> events = eventSRV.getUncompletedEvents();

            if (!CollectionUtils.isEmpty(events)) {
                for (final EventETY event : events) {
                    int days = schedulerCFG.getKiloWThreshold();
                    if (TurbinePower.MEGAWATT.equals(TurbinePower.get(event.getPower()))) {
                        days = schedulerCFG.getMegaWThreshold();
                    }
                    
                    if (LocalDate.parse(event.getStartingDateEEMM()).isBefore(LocalDate.now().minusDays(days))) {
                        sendMail(event, days);
                    } else {
                        log.info("Event with id: {} not expired yet, checking again at next schedule", event.getId());
                    }
                }
            }
        } catch (final Exception e) {
            log.error("Error while executing notification scheduler");
        }

        log.info("notification scheduler complete");
    }

    private void sendMail(final EventETY event, final int days) {
        if (!event.isMailSent()) {
            log.info("Sending mail for event with id: {}", event.getId());
            final String subject = "Superamento soglia turbina: " + event.getTurbineName();
            final String body = "La turbina identificata come: " + event.getTurbineName() + " e di tipologia: " + event.getPower() + " ha superato la soglia di " + days + " giorni";
            final boolean mailSent = mailSRV.sendMail(subject, body);
            
            if (mailSent) {
                eventSRV.setMailSent(event.getId());
            } else {
                log.error("Error while sending mail for event with id: {}", event.getId());
            }
        } else {
            log.info("Mail already sent for event with id: {}", event.getId());
        }
    }

}
