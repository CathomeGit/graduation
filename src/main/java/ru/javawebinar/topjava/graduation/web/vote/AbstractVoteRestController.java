package ru.javawebinar.topjava.graduation.web.vote;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.service.VoteService;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractVoteRestController {

    @Autowired
    protected VoteService service;

    public Vote get(int userId, int id) {
        return service.get(id, userId);
    }

    public List<Vote> getAll(int userId) {
        return service.getAll(userId);
    }

    public List<Vote> getBetweenDates(int userId, LocalDate startDate, LocalDate endDate) {
        return service.getBetween(userId, startDate, endDate);
    }
}