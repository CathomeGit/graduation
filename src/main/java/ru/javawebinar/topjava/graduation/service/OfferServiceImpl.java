package ru.javawebinar.topjava.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.graduation.model.Dish;
import ru.javawebinar.topjava.graduation.model.Offer;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.repository.JpaDishRepository;
import ru.javawebinar.topjava.graduation.repository.JpaOfferRepository;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class OfferServiceImpl implements OfferService {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JpaOfferRepository offerRepository;
    private final JpaDishRepository dishRepository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public OfferServiceImpl(JpaOfferRepository offerRepository, JpaDishRepository dishRepository,
                            JpaRestaurantRepository restaurantRepository) {
        this.offerRepository = offerRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Offer get(int id, int restaurantId) {
        logger.info("get offer {} for restaurant {}", id, restaurantId);
        return checkNotFoundWithId(offerRepository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    @Override
    public List<Offer> getAll(int restaurantId) {
        logger.info("get all offers for restaurant {}", restaurantId);
        return offerRepository.findAllByRestaurantId(restaurantId, SORT_DATE);
    }

    @Override
    public List<Offer> getBetweenDates(int restaurantId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        logger.info("get offers for the period {} - {} for restaurant {}", startDate, endDate, restaurantId);
        return offerRepository.findAllByRestaurantIdAndDateBetween(restaurantId, adjustStartDate(startDate),
                adjustEndDate(endDate), SORT_DATE);
    }

    @Override
    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public void saveAll(@NotNull List<Offer> offers, int restaurantId) {
        logger.info("save current offers for restaurant {}", restaurantId);
        LocalDate date = getZoneAwareCurrentDate();
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        for (Offer offer : offers) {
            Dish dish = offer.getDish();
            Assert.notNull(dish, "Dish is mandatory");
            Dish fetchDish = dish.getId() == null ? dishRepository.getByRestaurantIdAndName(restaurantId, dish.getName()) :
                    dishRepository.getOne(dish.getId());
            if (fetchDish == null) {
                dish.setRestaurant(restaurant);
                dish = dishRepository.save(dish);
            } else {
                dish = fetchDish;
            }
            offer.setDate(date);
            offer.setDish(dish);
            offer.setRestaurant(restaurant);
        }
        offerRepository.deleteAllOnDate(restaurantId, date);
        offerRepository.saveAll(offers);
    }
}