package ru.javawebinar.topjava.graduation.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Restaurant;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface RestaurantService {

    Restaurant create(@NotNull Restaurant restaurant);

    void delete(int id);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    Restaurant get(int id);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<Restaurant> getAll();

    void update(@NotNull Restaurant restaurant);

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    List<Restaurant> getWithCurrentOffers(LocalDate date);
}
