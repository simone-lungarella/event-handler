package it.os.event.handler.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import it.os.event.handler.entity.EventETY;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IEventRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class EventRepo implements IEventRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer save(final EventETY entity) {

        try {
            entityManager.persist(entity);
            return entity.getId();
        } catch (Exception e) {
            log.error("Error encountered while persisting entity", e);
            throw new BusinessException("Error encountered while persisting entity", e);
        }
    }

    @Override
    public List<EventETY> getAllEvents() {

        try {
            return entityManager.createQuery("SELECT e FROM EventETY e", EventETY.class)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error encountered while retrieving ordered events", e);
            throw new BusinessException("Error encountered while retrieving ordered events", e);
        }
    }

    @Override
    public void deleteById(final Integer eventId) {

        try {
            entityManager.remove(entityManager.find(EventETY.class, eventId));
        } catch (Exception e) {
            log.error("Error encountered while deleting event", e);
            throw new BusinessException("Error encountered while deleting event", e);
        }

    }

    @Override
    public EventETY findById(Integer eventId) {
        try {
            return entityManager.createQuery("SELECT E FROM EventETY E WHERE E.id = (:eventId)", EventETY.class)
                    .setParameter("eventId", eventId)
                    .getSingleResult();
        } catch (Exception e) {
            log.error("Error encountered while retrieving event", e);
            throw new BusinessException("Error encountered while retrieving event", e);
        }
    }

    @Override
    public void update(EventETY event) {
        try {
            entityManager.merge(event);
        } catch (Exception e) {
            log.error("Error encountered while updating event", e);
            throw new BusinessException("Error encountered while updating event", e);
        }
    }

    @Override
    public List<EventETY> getAllCompletedEvents() {

        try {
            return entityManager.createQuery("SELECT e FROM EventETY e WHERE e.completionDate != null", EventETY.class)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error encountered while retrieving events to retain", e);
            throw new BusinessException("Error encountered while retrieving ordered events to retain", e);
        }
    }

    @Override
    public void deleteAll() {

        try {
            final List<EventETY> events = entityManager.createQuery("SELECT E FROM EventETY E", EventETY.class).getResultList();
            if (!CollectionUtils.isEmpty(events)) {
                events.forEach(event -> entityManager.remove(event));
            }
        } catch (Exception e) {
            log.error("Error encountered while deleting all events", e);
            throw new BusinessException("Error encountered while deleting all events", e);
        }

    }

    @Override
    public void setMailSent(Integer id) {
        
        try {
            entityManager.createQuery("UPDATE EventETY E SET E.mailSent = true WHERE E.id = (:id)")
                    .setParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            log.error("Error encountered while setting mail sent", e);
            throw new BusinessException("Error encountered while setting mail sent", e);
        }
    }

    @Override
    public List<EventETY> getUncompletedEvents() {
        
        try {
            return entityManager.createQuery("SELECT e FROM EventETY e WHERE e.completionDate IS NULL", EventETY.class)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error encountered while retrieving uncompleted events", e);
            throw new BusinessException("Error encountered while retrieving uncompleted events", e);
        }
    }

}
