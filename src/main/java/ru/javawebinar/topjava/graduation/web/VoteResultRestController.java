package ru.javawebinar.topjava.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.graduation.service.VoteService;
import ru.javawebinar.topjava.graduation.to.VoteResultTo;
import ru.javawebinar.topjava.graduation.util.exception.LockedException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.graduation.web.VoteResultRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteResultRestController {

    static final String REST_URL = "/rest/vote-results";

    @Autowired
    VoteService service;

    @GetMapping
    public List<VoteResultTo> current() {
        if (ZonedDateTime.now(TIMEZONE).getHour() < RESTRICTION_HOUR) {
            throw new LockedException("Voting is not finished");
        }
        return service.voteResults(getZoneAwareCurrentDate());
    }

    @GetMapping("/history")
    public List<VoteResultTo> history(@RequestParam LocalDate date) {
        if (date.equals(getZoneAwareCurrentDate())) {
            return current();
        }
        return service.voteResults(date);
    }
}