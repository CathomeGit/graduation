package ru.javawebinar.topjava.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Restaurant;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.repository.JpaRestaurantRepository;
import ru.javawebinar.topjava.graduation.repository.JpaUserRepository;
import ru.javawebinar.topjava.graduation.repository.JpaVoteRepository;
import ru.javawebinar.topjava.graduation.to.VoteResultTo;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.graduation.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.graduation.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class VoteServiceImpl implements VoteService {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "date");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JpaVoteRepository voteRepository;
    private final JpaRestaurantRepository restaurantRepository;
    private final JpaUserRepository userRepository;

    @Autowired
    public VoteServiceImpl(JpaRestaurantRepository restaurantRepository, JpaVoteRepository voteRepository, JpaUserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Vote get(int id, int userId) {
        logger.info("get vote {} for user {}", id, userId);
        return checkNotFoundWithId(voteRepository.findByIdAndUserId(id, userId), id);
    }

    @Override
    public List<Vote> getAll(int userId) {
        logger.info("get all votes for user {}", userId);
        return voteRepository.findAllByUserId(userId, SORT_DATE);
    }

    @Override
    public List<Vote> getBetween(int userId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        logger.info("get votes between dates {} - {} for user {}", startDate, endDate, userId);
        return voteRepository.findAllByUserIdAndDateBetween(userId, adjustStartDate(startDate),
                adjustEndDate(endDate), SORT_DATE);
    }

    @Override
    @Transactional
    public Vote create(int userId, int restaurantId) {
        logger.info("create vote for the restaurant {} of user {}", restaurantId, userId);
        LocalDate date = getZoneAwareCurrentDate();
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        User user = userRepository.getOne(userId);
        Vote vote = new Vote(date, restaurant, user);
        voteRepository.deleteOnDate(userId, date);
        return voteRepository.save(vote);
    }

    @Override
    @Cacheable(value = "results")
    public List<VoteResultTo> voteResults(@NotNull LocalDate date) {
        logger.info("get vote results on {}", date);
        List<VoteResultTo> results = voteRepository.retrieveVoteResult(date);
        // https://stackoverflow.com/a/39192050/4925022 -->
        for (VoteResultTo result : results) {
            if (result.getDate() == null) {
                result.setDate(date);
            }
        }
        // <--
        return results;
    }
}