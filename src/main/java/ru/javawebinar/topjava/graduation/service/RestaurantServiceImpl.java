package ru.javawebinar.topjava.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
    private static final Sort SORT_NAME = new Sort(Sort.Direction.ASC, "name");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JpaRestaurantRepository repository;

    @Autowired
    public RestaurantServiceImpl(JpaRestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public Restaurant create(@NotNull Restaurant restaurant) {
        logger.info("create restaurant {}", restaurant);
        return repository.save(restaurant);
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public void delete(int id) {
        logger.info("delete restaurant {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Restaurant get(int id) {
        logger.info("get restaurant {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Restaurant> getAll() {
        logger.info("get all restaurants");
        return repository.findAll(SORT_NAME);
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public void update(@NotNull Restaurant restaurant) {
        logger.info("update restaurant {}", restaurant);
        repository.save(restaurant);
    }

    @Override
    @Cacheable(value = "current-offers")
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Restaurant> getWithCurrentOffers(LocalDate date) {
        logger.info("get restaurants with offers on date {}", date);
        return repository.findAllByOffers_date(date, SORT_NAME);
    }
}