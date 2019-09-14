package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Course;
import ru.javawebinar.topjava.graduation.model.Offer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

public class OfferTestData {
    public final static Offer PRIME_01_01 = new Offer(START_SEQ + 28, new Course(START_SEQ + 12, "Салат с тунцом", PRIME),
            PRIME, LocalDate.of(2019, 8, 1), 25000);
    public final static Offer PRIME_01_02 = new Offer(START_SEQ + 29, new Course(START_SEQ + 13, "Лапша со свининой", PRIME),
            PRIME, LocalDate.of(2019, 8, 1), 30000);
    public final static Offer PRIME_01_03 = new Offer(START_SEQ + 30, new Course(START_SEQ + 14, "Матча латте", PRIME),
            PRIME, LocalDate.of(2019, 8, 1), 30000);
    public final static Offer PRIME_02_01 = new Offer(START_SEQ + 31, new Course(START_SEQ + 15, "Салат цезарь", PRIME),
            PRIME, LocalDate.of(2019, 8, 2), 25000);
    public final static Offer PRIME_02_02 = new Offer(START_SEQ + 32, new Course(START_SEQ + 16, "Паста с брокколи", PRIME),
            PRIME, LocalDate.of(2019, 8, 2), 30000);
    public final static Offer PRIME_02_03 = new Offer(START_SEQ + 33, new Course(START_SEQ + 17, "Раф", PRIME),
            PRIME, LocalDate.of(2019, 8, 2), 30000);

    public final static List<Offer> PRIME_01 = List.of(PRIME_01_01, PRIME_01_02, PRIME_01_03);
    public final static List<Offer> PRIME_02 = List.of(PRIME_02_01, PRIME_02_02, PRIME_02_03);

    public final static Offer KFC_01_01 = new Offer(START_SEQ + 34, new Course(START_SEQ + 18, "Тёмный бургер", KFC),
            KFC, LocalDate.of(2019, 8, 1), 19900);
    public final static Offer KFC_01_02 = new Offer(START_SEQ + 35, new Course(START_SEQ + 19, "Капучино", KFC),
            KFC, LocalDate.of(2019, 8, 1), 9900);
    public final static Offer KFC_02_01 = new Offer(START_SEQ + 36, new Course(START_SEQ + 20, "Твистер острый", KFC),
            KFC, LocalDate.of(2019, 8, 2), 18000);
    public final static Offer KFC_02_02 = new Offer(START_SEQ + 37, new Course(START_SEQ + 21, "Чай в ассортименте", KFC),
            KFC, LocalDate.of(2019, 8, 2), 8900);

    public final static List<Offer> KFC_01 = List.of(KFC_01_01, KFC_01_02);
    public final static List<Offer> KFC_02 = List.of(KFC_02_01, KFC_02_02);

    public final static Offer MUMU_01_01 = new Offer(START_SEQ + 38, new Course(START_SEQ + 22, "Капустный салат", MUMU),
            MUMU, LocalDate.of(2019, 8, 1), 5990);
    public final static Offer MUMU_01_02 = new Offer(START_SEQ + 39, new Course(START_SEQ + 23, "Борщ", MUMU),
            MUMU, LocalDate.of(2019, 8, 1), 11990);
    public final static Offer MUMU_01_03 = new Offer(START_SEQ + 40, new Course(START_SEQ + 24, "Квас", MUMU),
            MUMU, LocalDate.of(2019, 8, 1), 8990);
    public final static Offer MUMU_02_01 = new Offer(START_SEQ + 41, new Course(START_SEQ + 25, "Винегрет", MUMU),
            MUMU, LocalDate.of(2019, 8, 2), 10080);
    public final static Offer MUMU_02_02 = new Offer(START_SEQ + 42, new Course(START_SEQ + 26, "Домашняя котлета с картофельным пюре", MUMU),
            MUMU, LocalDate.of(2019, 8, 2), 15150);
    public final static Offer MUMU_02_03 = new Offer(START_SEQ + 43, new Course(START_SEQ + 27, "Американо со сливками", MUMU),
            MUMU, LocalDate.of(2019, 8, 2), 12020);

    public final static List<Offer> MUMU_01 = List.of(MUMU_01_01, MUMU_01_02, MUMU_01_03);
    public final static List<Offer> MUMU_02 = List.of(MUMU_02_01, MUMU_02_02, MUMU_02_03);

    private OfferTestData() {
    }

    public static void assertMatch(Offer actual, Offer expected) {
        assertThat(actual).isEqualToIgnoringGivenFields("restaurant");
    }

    public static void assertMatch(Iterable<Offer> actual, Offer... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Offer> actual, Iterable<Offer> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }
}