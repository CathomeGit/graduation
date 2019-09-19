package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void create() {
        Restaurant newRestaurant = getCreated();
        Restaurant created = service.create(new Restaurant(newRestaurant));
        newRestaurant.setId(created.getId());
        assertMatch(created, newRestaurant);
        assertMatch(service.getAll(), KFC, PRIME, MUMU, newRestaurant);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Restaurant(null, "Му-Му")));
    }

    @Test
    void deleteRestricted() {
        assertThrows(DataIntegrityViolationException.class, () -> service.delete(MUMU.getId()));
    }

    @Test
    void delete() {
        Restaurant created = service.create(getCreated());
        service.delete(created.getId());
        assertMatch(service.getAll(), KFC, PRIME, MUMU);
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(MUMU.getId());
        assertMatch(restaurant, MUMU);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        service.update(updated);
        assertMatch(service.get(updated.getId()), updated);
    }

    @Test
    void updateDuplicate() {
        Restaurant updated = getUpdated();
        updated.setName(MUMU.getName());
        assertThrows(DataAccessException.class, () -> service.update(updated));
    }

    @Test
    void getAll() {
        List<Restaurant> all = service.getAll();
        assertMatch(all, KFC, PRIME, MUMU);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> service.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
    }
}