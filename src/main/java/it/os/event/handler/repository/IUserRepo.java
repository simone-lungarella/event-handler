package it.os.event.handler.repository;

import java.util.List;
import java.util.Optional;

import it.os.event.handler.entity.UserETY;

public interface IUserRepo {

    Optional<UserETY> findByUsername(String username);

    void save(UserETY user);

    List<UserETY> findAll();

    void deleteByUsername(String username);
}
