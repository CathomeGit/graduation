package ru.javawebinar.topjava.graduation.web.vote;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.util.DateTimeUtil;
import ru.javawebinar.topjava.graduation.util.exception.ModificationRestrictionException;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.RESTRICTION_HOUR;
import static ru.javawebinar.topjava.graduation.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.graduation.web.vote.ProfileVoteRestController.USER_URL;

@RestController
@RequestMapping(value = USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteRestController extends AbstractVoteRestController {

    public static final String USER_URL = "/rest/profile/votes";

    @GetMapping
    public List<Vote> getAll() {
        return super.getAll(authUserId());
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        return super.get(authUserId(), id);
    }

    @GetMapping("/filter")
    public List<Vote> getBetweenDates(@RequestParam(required = false) LocalDate startDate,
                                      @RequestParam(required = false) LocalDate endDate) {
        return super.getBetweenDates(authUserId(), startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<Vote> vote(@RequestParam int restaurantId) {
        if (ZonedDateTime.now(DateTimeUtil.TIMEZONE).getHour() >= RESTRICTION_HOUR) {
            throw new ModificationRestrictionException("Votes are not accepted after " + RESTRICTION_HOUR);
        }

        Vote created = service.create(authUserId(), restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(USER_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}