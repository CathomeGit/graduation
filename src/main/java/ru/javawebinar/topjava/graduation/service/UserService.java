package ru.javawebinar.topjava.graduation.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface UserService {

    User create(@NotNull User user);

    void delete(int id);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    User get(int id);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    User getByEmail(@NotBlank String email);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<User> getAll();

    void update(@NotNull User user);

    void enable(int id, boolean enabled);
}
