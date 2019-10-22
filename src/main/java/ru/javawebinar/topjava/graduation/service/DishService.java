package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Dish;
import ru.javawebinar.topjava.graduation.repository.JpaDishRepository;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {
    private static final Sort SORT_NAME = new Sort(Sort.Direction.ASC, "name");
    private final JpaDishRepository repository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public DishService(JpaDishRepository repository, JpaRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public Dish create(@NotNull Dish dish, int restaurantId) {
        return save(dish, restaurantId);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId) != 0, id);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    public List<Dish> getAll(int restaurantId) {
        return repository.findAllByRestaurantId(restaurantId, SORT_NAME);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public void update(@NotNull Dish dish, int restaurantId) {
        save(dish, restaurantId);
    }

    private Dish save(@NotNull Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }
}