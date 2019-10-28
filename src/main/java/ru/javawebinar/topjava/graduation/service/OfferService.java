package ru.javawebinar.topjava.graduation.service;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Offer;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface OfferService {
    Offer get(int id, int restaurantId);

    List<Offer> getAll(int restaurantId);

    List<Offer> getBetweenDates(int restaurantId, @Nullable LocalDate startDate, @Nullable LocalDate endDate);

    @Transactional
    void saveAll(@NotNull List<Offer> offers, int restaurantId);
}
