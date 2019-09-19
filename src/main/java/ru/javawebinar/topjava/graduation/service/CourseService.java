package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.repository.JpaCourseRepository;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class CourseService {
    private static final Sort SORT_NAME = new Sort(Sort.Direction.ASC, "name");
    private final JpaCourseRepository repository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public CourseService(JpaCourseRepository repository, JpaRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public Course create(@NotNull Course course, int restaurantId) {
        return save(course, restaurantId);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId) != 0, id);
    }

    public Course get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    public List<Course> getAll(int restaurantId) {
        return repository.findAllByRestaurantId(restaurantId, SORT_NAME);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public void update(@NotNull Course course, int restaurantId) {
        save(course, restaurantId);
    }

    private Course save(@NotNull Course course, int restaurantId) {
        course.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(course);
    }
}