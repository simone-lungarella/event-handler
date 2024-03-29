package it.os.event.handler.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.StepTypeEnum;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IEventRepo;
import it.os.event.handler.service.IEventSRV;
import it.os.event.handler.service.IStepSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EventSRV implements IEventSRV {

    /**
     * Separator for CSV file - EU standard.
     */
    private static final char CSV_SEPARATOR = ';';

    @Autowired
    private IEventRepo eventRepo;

    @Autowired
    private IStepSRV stepSrv;

    @Override
    public List<EventETY> getOrderedEvents(boolean includeCompleted) {

        final List<EventETY> orderedEvents = new ArrayList<>();
        try {
            
            List<EventETY> events = new ArrayList<>();
            if (includeCompleted) {
                events = eventRepo.getAllEvents();
            } else {
                events = eventRepo.getUncompletedEvents();
            }

            log.info("Ordering events by their completion percentage");
            if (!CollectionUtils.isEmpty(events)) {
                log.info("Ordering same percentage events by their creation date");
                for (int i = 0; i <= StepTypeEnum.values().length; i++) {
                    final int currentStep = i;

                    final List<EventETY> samePercentage = events.stream()
                            .filter(ev -> currentStep == ev.getCompletedSteps()).collect(Collectors.toList());
                    samePercentage.sort((ev1, ev2) -> ev1.getCreationDate().compareTo(ev2.getCreationDate()));
                    orderedEvents.addAll(samePercentage);
                }
            }
        } catch (final Exception e) {
            log.error("Error encountered while retrieving ordered events.", e);
            throw new BusinessException("Error encountered while retrieving ordered events.", e);
        }

        return orderedEvents;
    }

    @Override
    public boolean insertNewEvent(final EventRequest requestBody) {

        boolean isSuccessful = false;
        try {

            EventETY event = new EventETY(requestBody.getTurbineName(), requestBody.getTurbineNumber(),
                    requestBody.getDescription(), requestBody.getPower(), requestBody.getOperation(),
                    new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()), requestBody.getTurbineState());

            event.setToNotDismantle(requestBody.getToNotDismantle() != null ? requestBody.getToNotDismantle() : false);
            event.setOdlNumber(requestBody.getOdlNumber());
            event.setStartingDateEEMM(requestBody.getStartingDateEEMM() != null ? requestBody.getStartingDateEEMM().toString() : null);
            event.setStartingDateOOCC(requestBody.getStartingDateOOCC() != null ? requestBody.getStartingDateOOCC().toString() : null);
            event.setPermittingDate(requestBody.getPermittingDate() != null ? requestBody.getPermittingDate().toString() : null);
            event.setPriorNotification(requestBody.getPriorNotification() != null ? requestBody.getPriorNotification().toString() : null);

            final Integer eventId = eventRepo.save(event);

            stepSrv.saveAllSteps(stepSrv.generateDefaultSteps(eventId));

            isSuccessful = true;
        } catch (final Exception e) {
            log.error("Error encountered while persisting a new complete event.", e);
            throw new BusinessException("Error encountered while persisting a new complete event.", e);
        }

        return isSuccessful;
    }

    @Override
    public void deleteEvent(final Integer eventId) {

        try {
            eventRepo.deleteById(eventId);
            stepSrv.deleteAllByEventId(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while deleting an event.", e);
            throw new BusinessException("Error encountered while deleting an event.", e);
        }
    }

    @Override
    public EventETY findById(final Integer eventId) {
        try {
            return eventRepo.findById(eventId);
        } catch (final Exception e) {
            log.error("Error encountered while retrieving an event.", e);
            throw new BusinessException("Error encountered while retrieving an event.", e);
        }
    }

    @Override
    public void update(final EventETY event) {

        try {
            eventRepo.update(event);
        } catch (final Exception e) {
            log.error("Error encountered while updating an event.", e);
            throw new BusinessException("Error encountered while updating an event.", e);
        }

    }

    @Override
    public void updateEventStep(final StepETY step) {
        try {
            final EventETY event = findById(step.getEventId());

            if (step.isComplete()) {
                event.setCompletedSteps(event.getCompletedSteps() + 1);

                if (StepTypeEnum.CHIUSURA_PERMITTING.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));
                } else if (StepTypeEnum.COMPLETAMENTO_EEMM.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDateEEMM(LocalDate.now().toString());
                } else if (StepTypeEnum.COMPLETAMENTO_OOCC.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDateOOCC(LocalDate.now().toString());
                }

            } else {
                event.setCompletedSteps(event.getCompletedSteps() - 1);

                if (StepTypeEnum.CHIUSURA_PERMITTING.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDate(null);
                } else if (StepTypeEnum.COMPLETAMENTO_EEMM.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDateEEMM(null);
                } else if (StepTypeEnum.COMPLETAMENTO_OOCC.equals(StepTypeEnum.get(step.getName()))) {
                    event.setCompletionDateOOCC(null);
                }

            }
            update(event);
        } catch (final Exception e) {
            log.error("Error encountered while updating an event step.", e);
            throw new BusinessException("Error encountered while updating an event step.", e);
        }
    }

    @Override
    public List<EventETY> getAllCompletedEvents() {

        try {
            return eventRepo.getAllCompletedEvents();
        } catch (final Exception e) {
            log.error("Error encountered while retrieving completed events.", e);
            throw new BusinessException("Error encountered while retrieving completed events.", e);
        }
    }

    @Override
    public void deleteAllEvents() {

        try {
            eventRepo.deleteAll();
            stepSrv.deleteAllSteps();
        } catch (final Exception e) {
            log.error("Error encountered while deleting all events.", e);
            throw new BusinessException("Error encountered while deleting all events.", e);
        }
    }

    @Override
    public byte[] getTurbinesForExport() {

        final List<EventETY> events = getOrderedEvents(true);
        return writeToCsv(events);
    }

    public byte[] writeToCsv(List<EventETY> turbines) {

        try (
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                OutputStreamWriter streamWriter = new OutputStreamWriter(stream);
                CSVWriter writer = new CSVWriter(streamWriter);) {

            StringBuilder header = new StringBuilder("sep=,\n") // Specifying the separator
                    .append("Nome turbina, ")
                    .append("Numero turbina, ")
                    .append("Numero ODL, ")
                    .append("Descrizione, ")
                    .append("Data creazione, ")
                    .append("Stato turbina, ")
                    .append("Tipologia turbina, ")
                    .append("Operazioni, ")
                    .append("Inizio EEMM, ")
                    .append("Inizio OOCC, ")
                    .append("Fine EEMM, ")
                    .append("Fine OOCC, ")
                    .append("Smontaggio piazzola\n");

            streamWriter.write(header.toString());
            streamWriter.flush();
            StatefulBeanToCsv<EventETY> beanToCsv = new StatefulBeanToCsvBuilder<EventETY>(writer)
                    .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSV_SEPARATOR)
                    .withOrderedResults(true)
                    .build();

            beanToCsv.write(turbines);
            streamWriter.flush();

            return stream.toByteArray();
        } catch (Exception ex) {
            log.error("Error while exporting data as CSV file", ex);
            throw new BusinessException("Error while exporting data as CSV file", ex);
        }
    }

    @Override
    public void setMailSent(final Integer id) {
        try {
            eventRepo.setMailSent(id);
        } catch (Exception e) {
            log.error("Error while setting mail sent", e);
            throw new BusinessException("Error while setting mail sent", e);
        }
    }

    @Override
    public List<EventETY> getUncompletedEvents() {
        try {
            return eventRepo.getUncompletedEvents();
        } catch (Exception e) {
            log.error("Error while getting uncompleted events", e);
            throw new BusinessException("Error while getting uncompleted events", e);
        }
    }

}
