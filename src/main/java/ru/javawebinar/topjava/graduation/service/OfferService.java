package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
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
public class OfferService {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");

    private final JpaOfferRepository offerRepository;
    private final JpaDishRepository dishRepository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public OfferService(JpaOfferRepository offerRepository, JpaDishRepository dishRepository,
                        JpaRestaurantRepository restaurantRepository) {
        this.offerRepository = offerRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Offer get(int id, int restaurantId) {
        return checkNotFoundWithId(offerRepository.findByIdAndRestaurantId(id, restaurantId), id);
    }

    public List<Offer> getAll(int restaurantId) {
        return offerRepository.findAllByRestaurantId(restaurantId, SORT_DATE);
    }

    public List<Offer> getBetweenDates(int restaurantId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return offerRepository.findAllByRestaurantIdAndDateBetween(restaurantId, adjustStartDate(startDate),
                adjustEndDate(endDate), SORT_DATE);
    }

    @CacheEvict(value = "current-offers", allEntries = true)
    @Transactional
    public void saveAll(@NotNull List<Offer> offers, int restaurantId) {
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