package it.os.event.handler.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.os.event.handler.entity.UserETY;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IUserRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class UserRepo implements IUserRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserETY> findByUsername(String username) {

        Optional<UserETY> user = Optional.empty();
        try {

            user = Optional.ofNullable(entityManager
                    .createQuery("SELECT U FROM UserETY U WHERE U.username = (:username)", UserETY.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (final Exception e) {
            log.error("Error while retrieving user by username");
        }

        return user;
    }

    @Override
    public void save(UserETY user) {
        try {
            entityManager.persist(user);
        } catch (final Exception e) {
            log.error("Error while saving user", e);
            throw new BusinessException(String.format("Error while persisting user with username: %s", user.getUsername()), e);
        }
    }

    @Override
    public List<UserETY> findAll() {

        List<UserETY> users = null;
        try {
            users = entityManager.createQuery("SELECT U FROM UserETY U", UserETY.class).getResultList();
        } catch (final Exception e) {
            log.error("Error while retrieving all users", e);
        }

        return users;
    }

    @Override
    public void deleteByUsername(String username) {
        try {
            entityManager.createQuery("DELETE FROM UserETY U WHERE U.username = (:username)")
                    .setParameter("username", username)
                    .executeUpdate();
        } catch (final Exception e) {
            log.error("Error while deleting user by username", e);
            throw new BusinessException(String.format("Error while deleting user with username: %s", username), e);
        }
    }

}
