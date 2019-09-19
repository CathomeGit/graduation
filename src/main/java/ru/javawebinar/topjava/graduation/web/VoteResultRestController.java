package ru.javawebinar.topjava.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.javawebinar.topjava.graduation.model.VoteResult;
import ru.javawebinar.topjava.graduation.service.VoteResultService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.graduation.web.VoteResultRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteResultRestController {

    public static final String REST_URL = "/rest/vote-results";

    @Autowired
    VoteResultService service;

    @GetMapping
    public List<VoteResult> current() {
        if (ZonedDateTime.now(TIMEZONE).getHour() < RESTRICTION_HOUR) {
            throw new ResponseStatusException(HttpStatus.LOCKED, "Voting is not finished");
        }
        return service.createAndReturn(getZoneAwareCurrentDate());
    }

    @GetMapping("/history")
    public List<VoteResult> history(@RequestParam LocalDate date) {
        if (date.equals(getZoneAwareCurrentDate())) {
            return current();
        }
        return service.createAndReturn(date);
    }
}