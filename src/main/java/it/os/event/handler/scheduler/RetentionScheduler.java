package it.os.event.handler.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import it.os.event.handler.service.IEventSRV;

public class RetentionScheduler {
    
    @Value("${event.retention-days}")
    private Integer retentionDays;

    @Autowired
    private IEventSRV eventSRV;

    @Scheduled(cron = "${event.retention-schedule}")
    public void run() {
        
        // Get all events completed
        eventSRV.getOrderedEvents();
        // If event is completed and is older than retentionDays, delete it

    }
}
