package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.graduation.model.Role;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.*;

public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("users").clear();
    }

    @Test
    void create() {
        User newUser = getCreated();
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        assertMatch(created, newUser);
        assertMatch(service.getAll(), USER1, ADMIN, USER2, newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", USER1.getEmail(), "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() {
        service.delete(USER1.getId());
        assertMatch(service.getAll(), ADMIN, USER2);
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        User user = service.get(ADMIN.getId());
        assert !user.getPassword().equals(ADMIN.getPassword());
        assertMatch(user, ADMIN);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail(ADMIN.getEmail());
        assertMatch(user, ADMIN);
    }

    @Test
    void update() {
        User updated = getUpdated();
        service.update(updated);
        assertMatch(service.get(updated.getId()), updated);
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        assertMatch(all, USER1, ADMIN, USER2);
    }

    @Test
    void enable() {
        service.enable(USER1.getId(), false);
        assertFalse(service.get(USER1.getId()).isEnabled());
        service.enable(USER1.getId(), true);
        assertTrue(service.get(USER1.getId()).isEnabled());
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(
                new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)),
                ConstraintViolationException.class
        );
        validateRootCause(() -> service.create(
                new User(null, "User", "  ", "password", Role.ROLE_USER)),
                ConstraintViolationException.class
        );
        validateRootCause(() -> service.create(
                new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)),
                ConstraintViolationException.class
        );
    }
}