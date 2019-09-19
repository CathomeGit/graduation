package ru.javawebinar.topjava.graduation.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.MUMU;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.PRIME;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER1;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.USER2;
import static ru.javawebinar.topjava.graduation.testdata.VoteTestData.*;

class VoteServiceTest extends AbstractServiceTest {

    public static final LocalDate START_DATE = LocalDate.of(2019, 8, 1);
    public static final LocalDate END_DATE = LocalDate.of(2019, 8, 2);
    @Autowired
    protected VoteService service;

    @Test
    void vote() {
        service.create(USER1.getId(), PRIME.getId());
        Vote recorded = service.create(USER1.getId(), MUMU.getId());
        List<Vote> expected = service.getBetween(USER1.getId(), LocalDate.now(), LocalDate.now());
        assert expected.size() == 1;
        assertMatch(service.get(recorded.getId(), USER1.getId()), recorded);
    }

    @Test
    void get() {
        Vote vote = service.get(USER1_PRIME.getId(), USER1.getId());
        assertMatch(vote, USER1_PRIME);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, PRIME.getId()));
    }

    @Test
    void getAll() {
        List<Vote> all = service.getAll(USER1.getId());
        assertMatch(all, USER1_VOTES);
    }

    @Test
    void getBetween() {
        List<Vote> all = service.getBetween(USER1.getId(), START_DATE, START_DATE);
        assertMatch(all, USER1_PRIME);
        List<Vote> all2 = service.getBetween(USER2.getId(), START_DATE, END_DATE);
        assertMatch(all2, USER2_VOTES);
    }
}