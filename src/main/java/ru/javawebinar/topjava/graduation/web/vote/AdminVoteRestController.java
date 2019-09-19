package ru.javawebinar.topjava.graduation.web.vote;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.graduation.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.web.vote.AdminVoteRestController.ADMIN_URL;

@RestController
@RequestMapping(value = ADMIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController extends AbstractVoteRestController {

    public static final String ADMIN_URL = "/rest/admin/users/{userId}/votes";

    @GetMapping
    public List<Vote> getAll(@PathVariable int userId) {
        return super.getAll(userId);
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int userId, @PathVariable int id) {
        return super.get(userId, id);
    }

    @GetMapping("/filter")
    public List<Vote> getBetweenDates(@PathVariable int userId,
                                      @RequestParam(required = false) LocalDate startDate,
                                      @RequestParam(required = false) LocalDate endDate) {
        return super.getBetweenDates(userId, startDate, endDate);
    }
}