package it.os.event.handler.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
            entityManager.createQuery("DELETE FROM EventETY E WHERE E.id = (:eventId)")
                    .setParameter("eventId", eventId)
                    .executeUpdate();
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
            entityManager.createQuery("DELETE FROM EventETY E").executeUpdate();
        } catch (Exception e) {
            log.error("Error encountered while deleting all events", e);
            throw new BusinessException("Error encountered while deleting all events", e);
        }

    }

}
