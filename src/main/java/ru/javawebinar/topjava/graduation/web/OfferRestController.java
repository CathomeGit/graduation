package ru.javawebinar.topjava.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.graduation.model.Offer;
import ru.javawebinar.topjava.graduation.service.OfferService;
import ru.javawebinar.topjava.graduation.util.ValidationUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferRestController {

    private static final String ADMIN_URL = "/rest/admin/restaurants/{restaurantId}/offers";
    private static final String USER_URL = "/rest/profile/restaurants/{restaurantId}/offers";

    @Autowired
    protected OfferService service;

    @GetMapping(value = {ADMIN_URL, USER_URL})
    public List<Offer> getAll(@PathVariable int restaurantId) {
        return service.getAll(restaurantId);
    }

    @GetMapping(value = {ADMIN_URL + "/{id}", USER_URL + "/{id}"})
    public Offer get(@PathVariable int restaurantId, @PathVariable int id) {
        return service.get(id, restaurantId);
    }

    @GetMapping(value = {ADMIN_URL + "/filter", USER_URL + "/filter"})
    public List<Offer> getBetweenDates(@PathVariable int restaurantId,
                                       @RequestParam(required = false) LocalDate startDate,
                                       @RequestParam(required = false) LocalDate endDate) {
        return service.getBetweenDates(restaurantId, startDate, endDate);
    }

    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createAll(@PathVariable int restaurantId, @Valid @RequestBody List<Offer> offers) {
        offers.forEach(ValidationUtil::checkNew);
        service.saveAll(offers, restaurantId);
    }
}