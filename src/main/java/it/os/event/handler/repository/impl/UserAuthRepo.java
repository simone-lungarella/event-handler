package it.os.event.handler.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.os.event.handler.entity.UserAuthorizationsETY;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.repository.IUserAuthRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@Transactional
public class UserAuthRepo implements IUserAuthRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String getUserAuth(final String username) {

        String userAuthorization = null;
        try {
            final UserAuthorizationsETY userAuth = entityManager
                    .createQuery("SELECT U FROM UserAuthorizationsETY U WHERE U.username = (:username)",
                            UserAuthorizationsETY.class)
                    .setParameter("username", username)
                    .getSingleResult();

            if (userAuth != null) {
                userAuthorization = userAuth.getAuthorizations();
            }
        } catch (final Exception e) {
            log.error(String.format("Error while retrieving user auth for user with username: %s", username), e);
        }

        return userAuthorization;
    }

    @Override
    public void updateUserAuth(final String username, final String auth) {
        try {

            final UserAuthorizationsETY userAuth = entityManager
                    .createQuery("SELECT A FROM UserAuthorizationsETY A WHERE A.username = (:username)",
                            UserAuthorizationsETY.class)
                    .setParameter("username", username).getSingleResult();

            userAuth.setAuthorizations(auth);
            entityManager.merge(auth);
        } catch (NoResultException noRe) {
            log.warn("User auths not found, inserting new ones");
            final UserAuthorizationsETY newUserAuth = new UserAuthorizationsETY();
            newUserAuth.setUsername(username);
            newUserAuth.setAuthorizations(auth);
            entityManager.persist(newUserAuth);
        } catch (final Exception e) {
            log.error(String.format("Error while updating user auth for user with username: %s", username), e);
            throw new BusinessException(String.format("Error while updating user auth for user with username: %s", username), e);
        }
    }

}
