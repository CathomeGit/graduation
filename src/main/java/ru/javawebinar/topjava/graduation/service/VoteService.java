package ru.javawebinar.topjava.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;
import ru.javawebinar.topjava.graduation.repository.JpaUserRepository;
import ru.javawebinar.topjava.graduation.repository.JpaVoteRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");

    private final JpaVoteRepository voteRepository;
    private final JpaRestaurantRepository restaurantRepository;
    private final JpaUserRepository userRepository;

    @Autowired
    public VoteService(JpaRestaurantRepository restaurantRepository, JpaVoteRepository voteRepository, JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.findByIdAndUserId(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.findAllByUserId(userId, SORT_DATE);
    }

    public List<Vote> getBetween(int userId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        return voteRepository.findAllByUserIdAndDateBetween(userId, adjustStartDate(startDate),
                adjustEndDate(endDate), SORT_DATE);
    }

    @Transactional
    public Vote create(int userId, int restaurantId) {
        LocalDate date = getZoneAwareCurrentDate();
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        User user = userRepository.getOne(userId);
        Vote vote = new Vote(date, restaurant, user);
        voteRepository.deleteOnDate(userId, date);
        return voteRepository.save(vote);
    }
}