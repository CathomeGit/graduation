package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.model.Offer;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.repository.JpaCourseRepository;
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
    private final JpaCourseRepository courseRepository;
    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public OfferService(JpaOfferRepository offerRepository, JpaCourseRepository courseRepository,
                        JpaRestaurantRepository restaurantRepository) {
        this.offerRepository = offerRepository;
        this.courseRepository = courseRepository;
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
            Course course = offer.getCourse();
            Assert.notNull(course, "Course is mandatory");
            Course fetchCourse = course.getId() == null ? courseRepository.getByRestaurantIdAndName(restaurantId, course.getName()) :
                    courseRepository.getOne(course.getId());
            if (fetchCourse == null) {
                course.setRestaurant(restaurant);
                course = courseRepository.save(course);
            } else {
                course = fetchCourse;
            }
            offer.setDate(date);
            offer.setCourse(course);
            offer.setRestaurant(restaurant);
        }
        offerRepository.deleteAllOnDate(restaurantId, date);
        offerRepository.saveAll(offers);
    }
}