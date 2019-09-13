package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.UserTestData.*;

public class VoteTestData {
    public final static Vote USER1_PRIME = new Vote(START_SEQ + 6,
            LocalDate.of(2019, 8, 1), PRIME, USER1);
    public final static Vote USER1_MUMU = new Vote(START_SEQ + 7,
            LocalDate.of(2019, 8, 2), MUMU, USER1);
    public final static Vote USER2_PRIME = new Vote(START_SEQ + 8,
            LocalDate.of(2019, 8, 1), PRIME, USER2);
    public final static Vote USER2_KFC = new Vote(START_SEQ + 9,
            LocalDate.of(2019, 8, 2), KFC, USER2);
    public final static Vote ADMIN_MUMU = new Vote(START_SEQ + 10,
            LocalDate.of(2019, 8, 1), MUMU, ADMIN);
    public final static Vote ADMIN_KFC = new Vote(START_SEQ + 11,
            LocalDate.of(2019, 8, 2), KFC, ADMIN);

    // TODO
    //public final static List<Vote> ALL_SORTED =
    private VoteTestData() {
    }

    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields("user", "restaurant");
    }

    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user", "restaurant").isEqualTo(expected);
    }
}