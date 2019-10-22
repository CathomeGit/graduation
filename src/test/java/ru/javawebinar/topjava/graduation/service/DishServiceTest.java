package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javawebinar.topjava.graduation.model.Dish;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.graduation.testdata.DishTestData.assertMatch;
import static ru.javawebinar.topjava.graduation.testdata.DishTestData.getCreated;
import static ru.javawebinar.topjava.graduation.testdata.DishTestData.getUpdated;
import static ru.javawebinar.topjava.graduation.testdata.DishTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

class DishServiceTest extends AbstractServiceTest {
    @Autowired
    protected DishService service;

    @Test
    void create() {
        Dish newDish = getCreated();
        int restaurantId = newDish.getRestaurant().getId();
        Dish created = service.create(new Dish(newDish), restaurantId);
        newDish.setId(created.getId());
        assertMatch(created, newDish);
        List<Dish> expected = new LinkedList<>(PRIME_DISHES);
        expected.add(newDish);
        assertMatch(service.getAll(restaurantId), expected);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Dish(MUMU_01_DRINK.getName(), MUMU), MUMU.getId()));
    }

    @Test
    void deleteRestricted() {
        assertThrows(DataIntegrityViolationException.class, () -> service.delete(MUMU_01_DRINK.getId(), MUMU.getId()));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, PRIME.getId()));
    }

    @Test
    void deletedAnother() {
        assertThrows(NotFoundException.class, () ->
                service.delete(MUMU_01_DRINK.getId(), PRIME.getId()));
    }

    @Test
    void get() {
        Dish dish = service.get(PRIME_01_DRINK.getId(), PRIME.getId());
        assertMatch(dish, PRIME_01_DRINK);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, PRIME.getId()));
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        int restaurantId = updated.getRestaurant().getId();
        service.update(updated, restaurantId);
        assertMatch(service.get(updated.getId(), restaurantId), updated);
    }

    @Test
    void updateDuplicate() {
        Dish updated = getUpdated();
        int restaurantId = updated.getRestaurant().getId();
        updated.setName(PRIME_02_SALAD.getName());
        assertThrows(DataAccessException.class, () -> service.update(updated, restaurantId));
    }

    @Test
    void getAll() {
        List<Dish> all = service.getAll(KFC.getId());
        assertMatch(all, KFC_DISHES);
    }

    @Test
    void createWithException() throws Exception {
        int restaurantId = PRIME.getId();
        validateRootCause(() -> service.create(new Dish(" ", PRIME), restaurantId),
                ConstraintViolationException.class);
    }
}