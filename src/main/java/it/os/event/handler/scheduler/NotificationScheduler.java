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
                    
                    if (event.getPermittingDate() != null && !LocalDate.parse(event.getPermittingDate()).isAfter(LocalDate.now().minusDays(days))) {
                        if (!event.isMailSent()) {
                            sendMail(event, false);
                        } else {
                            log.info("Mail already sent once, checking if it's time to send again");
                            if (event.getPermittingDate() != null && LocalDate.parse(event.getPermittingDate()).equals(LocalDate.now().minusDays(schedulerCFG.getHigherThreshold()))) {
                                sendMail(event, true);
                            } else {
                                log.info("Mail sent already twice, skipping");
                            }
                        }
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

    private void sendMail(final EventETY event, final boolean isOvertime) {
        
        log.info("Sending mail for event with id: {}", event.getId());
        final String subject = String.format("Titolo abilitativo in scadenza per piazzola: %s", event.getTurbineName());
        String body = String.format("Il titolo abilitativo per la piazzola identificata con: I.%s %s e le opere civili accessorie, scadrà fra 20 giorni", 
            event.getTurbineNumber(), event.getTurbineName());

        if (isOvertime) {
            body = String.format("Il titolo abilitativo per la piazzola identificata con: I.%s %s e le opere civili accessorie è in scadenza oggi", 
            event.getTurbineNumber(), event.getTurbineName());
        }

        final boolean mailSent = mailSRV.sendMail(subject, body, isOvertime);
        
        if (mailSent) {
            eventSRV.setMailSent(event.getId());
        } else {
            log.error("Error while sending mail for event with id: {}", event.getId());
        }
    }

}
