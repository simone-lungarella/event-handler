package it.os.event.handler;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.repository.IEventRepo;
import it.os.event.handler.service.IEventSRV;

@SpringBootTest
@Profile("Test")
class EventTest {
    
    @Autowired
    IEventRepo eventRepo;

    @Autowired
    IEventSRV eventSrv;

    @Test
    void testEventRepository() {
        final String eventId = eventRepo.save(new EventETY("Test description", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date())));
        assertNotNull(eventId, "The event id should not be null");

        final List<EventETY> list = eventRepo.getAllIncompletedEvents();
        assertTrue(list.stream().map(EventETY::getId).collect(Collectors.toList()).contains(eventId), "The event inserted should be present in the database");
    }

    @Test
    void testEventSrv() {
        final boolean isPersisted = eventSrv.insertNewEvent("eventDescription");
        assertTrue(isPersisted, "The event should have been persisted");
    }
}
