package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private static final Sort SORT_NAME = new Sort(Sort.Direction.ASC, "name");
    private final JpaRestaurantRepository repository;

    @Autowired
    public RestaurantService(JpaRestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    public Restaurant create(@NotNull Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    public void update(@NotNull Restaurant restaurant) {
        repository.save(restaurant);
    }

    @Cacheable(value = "current-offers")
    public List<Restaurant> getWithCurrentOffers(LocalDate date) {
        return repository.findAllByOffers_date(date, SORT_NAME);
    }
}