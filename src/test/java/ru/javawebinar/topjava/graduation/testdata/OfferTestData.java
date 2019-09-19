package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.model.Offer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.graduation.testdata.CourseTestData.*;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

public class OfferTestData {
    public final static Offer PRIME_01_01 = new Offer(START_SEQ + 28, PRIME_01_SALAD,
            PRIME, LocalDate.of(2019, 8, 1), 25000);
    public final static Offer PRIME_01_02 = new Offer(START_SEQ + 29, PRIME_01_MAIN,
            PRIME, LocalDate.of(2019, 8, 1), 30000);
    public final static Offer PRIME_01_03 = new Offer(START_SEQ + 30, PRIME_01_DRINK,
            PRIME, LocalDate.of(2019, 8, 1), 30000);
    public final static Offer PRIME_02_01 = new Offer(START_SEQ + 31, PRIME_02_SALAD,
            PRIME, LocalDate.of(2019, 8, 2), 25000);
    public final static Offer PRIME_02_02 = new Offer(START_SEQ + 32, PRIME_02_MAIN,
            PRIME, LocalDate.of(2019, 8, 2), 30000);
    public final static Offer PRIME_02_03 = new Offer(START_SEQ + 33, PRIME_02_DRINK,
            PRIME, LocalDate.of(2019, 8, 2), 30000);

    public final static List<Offer> PRIME_01 = List.of(PRIME_01_01, PRIME_01_02, PRIME_01_03);
    public final static List<Offer> PRIME_02 = List.of(PRIME_02_01, PRIME_02_02, PRIME_02_03);

    public final static Offer KFC_01_01 = new Offer(START_SEQ + 34, KFC_01_MAIN,
            KFC, LocalDate.of(2019, 8, 1), 19900);
    public final static Offer KFC_01_02 = new Offer(START_SEQ + 35, KFC_01_DRINK,
            KFC, LocalDate.of(2019, 8, 1), 9900);
    public final static Offer KFC_02_01 = new Offer(START_SEQ + 36, KFC_02_MAIN,
            KFC, LocalDate.of(2019, 8, 2), 18000);
    public final static Offer KFC_02_02 = new Offer(START_SEQ + 37, KFC_02_DRINK,
            KFC, LocalDate.of(2019, 8, 2), 8900);

    public final static List<Offer> KFC_01 = List.of(KFC_01_01, KFC_01_02);
    public final static List<Offer> KFC_02 = List.of(KFC_02_01, KFC_02_02);

    public final static Offer MUMU_01_01 = new Offer(START_SEQ + 38, MUMU_01_SALAD,
            MUMU, LocalDate.of(2019, 8, 1), 5990);
    public final static Offer MUMU_01_02 = new Offer(START_SEQ + 39, MUMU_01_MAIN,
            MUMU, LocalDate.of(2019, 8, 1), 11990);
    public final static Offer MUMU_01_03 = new Offer(START_SEQ + 40, MUMU_01_DRINK,
            MUMU, LocalDate.of(2019, 8, 1), 8990);
    public final static Offer MUMU_02_01 = new Offer(START_SEQ + 41, MUMU_02_SALAD,
            MUMU, LocalDate.of(2019, 8, 2), 10080);
    public final static Offer MUMU_02_02 = new Offer(START_SEQ + 42, MUMU_02_MAIN,
            MUMU, LocalDate.of(2019, 8, 2), 15150);
    public final static Offer MUMU_02_03 = new Offer(START_SEQ + 43, MUMU_02_DRINK,
            MUMU, LocalDate.of(2019, 8, 2), 12020);

    public final static List<Offer> MUMU_01 = List.of(MUMU_01_01, MUMU_01_02, MUMU_01_03);
    public final static List<Offer> MUMU_02 = List.of(MUMU_02_01, MUMU_02_02, MUMU_02_03);

    private OfferTestData() {
    }

    public static List<Offer> getCreated() {
        Offer soup = new Offer(new Course("Борщ", MUMU), MUMU, LocalDate.now(), 11990); // existing
        Offer drink = new Offer(new Course("Напиток", MUMU), MUMU, LocalDate.now(), 8970); // new
        return List.of(soup, drink);
    }

    public static void assertMatch(Offer actual, Offer expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
    }

    public static void assertMatch(Iterable<Offer> actual, Offer... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Offer> actual, Iterable<Offer> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }
}