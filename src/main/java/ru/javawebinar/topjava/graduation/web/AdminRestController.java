package ru.javawebinar.topjava.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.service.UserService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestController {

    public static final String REST_URL = "/rest/admin/users";

    @Autowired
    protected UserService service;

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        checkNew(user);
        User created = service.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        assureIdConsistent(user, id);
        service.update(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
    }
}