package ru.javawebinar.topjava.graduation.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Dish;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface DishService {
    Dish create(@NotNull Dish dish, int restaurantId);

    void delete(int id, int restaurantId);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Dish get(int id, int restaurantId);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<Dish> getAll(int restaurantId);

    void update(@NotNull Dish dish, int restaurantId);
}