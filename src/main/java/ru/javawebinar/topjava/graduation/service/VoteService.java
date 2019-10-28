package ru.javawebinar.topjava.graduation.service;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.to.VoteResultTo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface VoteService {
    Vote get(int id, int userId);

    List<Vote> getAll(int userId);

    List<Vote> getBetween(int userId, @Nullable LocalDate startDate, @Nullable LocalDate endDate);

    @Transactional
    Vote create(int userId, int restaurantId);

    List<VoteResultTo> voteResults(@NotNull LocalDate date);
}
