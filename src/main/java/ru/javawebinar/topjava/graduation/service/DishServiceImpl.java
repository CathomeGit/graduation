package ru.javawebinar.topjava.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Dish;
import ru.javawebinar.topjava.graduation.repository.JpaDishRepository;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional
public class DishServiceImpl implements DishService {
    private static final Sort SORT_NAME = new Sort(Sort.Direction.ASC, "name");
    private final JpaDishRepository repository;
    private final JpaRestaurantRepository restaurantRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public DishServiceImpl(JpaDishRepository repository, JpaRestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public Dish create(@NotNull Dish dish, int restaurantId) {
        logger.info("create dish {} for restaurant {}", dish, restaurantId);
        return save(dish, restaurantId);
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public void delete(int id, int restaurantId) {
        logger.info("delete dish {} for restaurant {}", id, restaurantId);
        checkNotFoundWithId(repository.delete(id, restaurantId) != 0, id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Dish get(int id, int restaurantId) {
        logger.info("get dish {} for restaurant {}", id, restaurantId);
        return checkNotFoundWithId(repository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Dish> getAll(int restaurantId) {
        logger.info("get all dishes for restaurant {}", restaurantId);
        return repository.findAllByRestaurantId(restaurantId, SORT_NAME);
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    public void update(@NotNull Dish dish, int restaurantId) {
        logger.info("update dish {} for restaurant {}", dish, restaurantId);
        save(dish, restaurantId);
    }

    private Dish save(@NotNull Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }
}