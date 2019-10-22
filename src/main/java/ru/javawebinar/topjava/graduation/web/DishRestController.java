package ru.javawebinar.topjava.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.graduation.model.Dish;
import ru.javawebinar.topjava.graduation.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {

    public static final String ADMIN_URL = "/rest/admin/restaurants/{restaurantId}/dishes";
    public static final String USER_URL = "/rest/profile/restaurants/{restaurantId}/dishes";

    @Autowired
    protected DishService service;

    @GetMapping(value = {ADMIN_URL, USER_URL})
    public List<Dish> getAll(@PathVariable int restaurantId) {
        return service.getAll(restaurantId);
    }

    @GetMapping(value = {ADMIN_URL + "/{id}", USER_URL + "/{id}"})
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        return service.get(id, restaurantId);
    }

    @PostMapping(value = ADMIN_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restaurantId,
                                                   @Valid @RequestBody Dish dish) {
        checkNew(dish);
        Dish created = service.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(value = ADMIN_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        service.delete(id, restaurantId);
    }

    @PutMapping(value = ADMIN_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable int restaurantId,
                       @PathVariable int id,
                       @Valid @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        service.update(dish, restaurantId);
    }
}