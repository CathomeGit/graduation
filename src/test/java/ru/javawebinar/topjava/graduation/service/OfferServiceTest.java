package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.graduation.model.Offer;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.graduation.testdata.OfferTestData.assertMatch;
import static ru.javawebinar.topjava.graduation.testdata.OfferTestData.getCreated;
import static ru.javawebinar.topjava.graduation.testdata.OfferTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

class OfferServiceTest extends AbstractServiceTest {
    @Autowired
    protected OfferService service;

    @Test
    void create() {
        List<Offer> expected = getCreated();
        service.saveAll(expected, MUMU.getId());
        List<Offer> created = service.getBetweenDates(MUMU.getId(), LocalDate.now(), LocalDate.now());
        assertMatch(created, expected);
    }

    @Test
    void get() {
        Offer offer = service.get(MUMU_01_01.getId(), MUMU.getId());
        assertMatch(offer, MUMU_01_01);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, PRIME.getId()));
    }

    @Test
    void getAll() {
        List<Offer> all = service.getAll(KFC.getId());
        List<Offer> expected = new LinkedList<>(KFC_02);
        expected.addAll(KFC_01);
        assertMatch(all, expected);
    }
}