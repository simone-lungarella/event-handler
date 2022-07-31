package it.os.event.handler.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.StepETY;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IStepRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class StepRepo implements IStepRepo {

    private static final String SELECT_STEPS_QUERY = "SELECT S FROM StepETY S";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer save(final StepETY step) {

        try {
            entityManager.persist(step);
            return step.getId();
        } catch (final Exception e) {
            log.error("Error while saving a single event step", e);
            throw new BusinessException("Error while saving a single event step", e);
        }
    }

    @Override
    public void saveAll(final List<StepETY> steps) {

        try {
            if (!CollectionUtils.isEmpty(steps)) {
                steps.forEach(step -> entityManager.persist(step));
            }
        } catch (final Exception e) {
            log.error("Error while inserting steps", e);
            throw new BusinessException("Error while inserting steps", e);
        }
    }

    @Override
    public List<StepETY> getStepsByEventId(final Integer eventId) {
        try {
            return entityManager
                    .createQuery("SELECT S FROM StepETY S WHERE S.eventId = (:eventId)", StepETY.class)
                    .setParameter("eventId", eventId)
                    .getResultList();
        } catch (final Exception e) {
            log.error("Error while retrieving all steps associated to an event", e);
            throw new BusinessException("Error while retrieving all steps associated to an event", e);
        }
    }

    @Override
    public void deleteAllByEventId(Integer eventId) {

        try {
            final List<StepETY> steps = entityManager
                .createQuery(SELECT_STEPS_QUERY + " WHERE S.eventId = (:eventId)", StepETY.class)
                .setParameter("eventId", eventId)
                .getResultList();

            if (!CollectionUtils.isEmpty(steps)) {
                steps.forEach(step -> entityManager.remove(step));
            }
        } catch (final Exception e) {
            log.error(String.format("Error while deleting all steps associated to the event with id: %s", eventId), e);
            throw new BusinessException(String.format("Error while deleting all steps associated to the event with id: %s", eventId), e);
        }
    }

    @Override
    public StepETY update(StepETY step) {
        try {
            return entityManager.merge(step);
        } catch (Exception e) {
            log.error("Error while updating a single event step", e);
            throw new BusinessException("Error while updating a single event step", e);
        }
    }

    @Override
    public StepETY findById(Integer stepId) {
        try {
            return entityManager.find(StepETY.class, stepId);
        } catch (Exception e) {
            log.error(String.format("Error while retrieving step with id: %s", stepId), e);
            throw new BusinessException(String.format("Error while retrieving step with id: %s", stepId), e);
        }
    }

    @Override
    public List<StepETY> findAll() {

        try {
            return entityManager.createQuery(SELECT_STEPS_QUERY, StepETY.class).getResultList();
        } catch (Exception e) {
            log.error("Error while retrieving all steps", e);
            throw new BusinessException("Error while retrieving all steps", e);
        }
    }

    @Override
    public void deleteAll() {
        
        try {
            final List<StepETY> steps = entityManager.createQuery(SELECT_STEPS_QUERY, StepETY.class).getResultList();
            if (!CollectionUtils.isEmpty(steps)) {
                steps.forEach(step -> entityManager.remove(step));
            }
        } catch (Exception e) {
            log.error("Error while deleting all steps", e);
            throw new BusinessException("Error while deleting all steps", e);
        }
    }

}
