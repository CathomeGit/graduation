package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.assertMatch;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.getCreated;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.getUpdated;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

class CourseServiceTest extends AbstractServiceTest {
    @Autowired
    protected CourseService service;

    @Test
    void create() {
        Course newCourse = getCreated();
        int restaurantId = newCourse.getRestaurant().getId();
        Course created = service.create(new Course(newCourse), restaurantId);
        newCourse.setId(created.getId());
        assertMatch(created, newCourse);
        List<Course> expected = new LinkedList<>(PRIME_COURSES);
        expected.add(newCourse);
        assertMatch(service.getAll(restaurantId), expected);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Course(MUMU_01_DRINK.getName(), MUMU), MUMU.getId()));
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
        Course course = service.get(PRIME_01_DRINK.getId(), PRIME.getId());
        assertMatch(course, PRIME_01_DRINK);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, PRIME.getId()));
    }

    @Test
    void update() {
        Course updated = getUpdated();
        int restaurantId = updated.getRestaurant().getId();
        service.update(updated, restaurantId);
        assertMatch(service.get(updated.getId(), restaurantId), updated);
    }

    @Test
    void updateDuplicate() {
        Course updated = getUpdated();
        int restaurantId = updated.getRestaurant().getId();
        updated.setName(PRIME_02_SALAD.getName());
        assertThrows(DataAccessException.class, () -> service.update(updated, restaurantId));
    }

    @Test
    void getAll() {
        List<Course> all = service.getAll(KFC.getId());
        assertMatch(all, KFC_COURSES);
    }

    @Test
    void createWithException() throws Exception {
        int restaurantId = PRIME.getId();
        validateRootCause(() -> service.create(new Course(" ", PRIME), restaurantId),
                ConstraintViolationException.class);
    }
}