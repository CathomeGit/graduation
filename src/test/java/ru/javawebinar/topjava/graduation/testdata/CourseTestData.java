package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Course;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

public class CourseTestData {
    public final static Course PRIME_01_01 = new Course(START_SEQ + 12, "Салат с тунцом", PRIME,
            LocalDate.of(2019, 8, 1), 25000);
    public final static Course PRIME_01_02 = new Course(START_SEQ + 13, "Лапша со свининой", PRIME,
            LocalDate.of(2019, 8, 1), 30000);
    public final static Course PRIME_01_03 = new Course(START_SEQ + 14, "Матча латте", PRIME,
            LocalDate.of(2019, 8, 1), 30000);
    public final static Course PRIME_02_01 = new Course(START_SEQ + 15, "Салат цезарь", PRIME,
            LocalDate.of(2019, 8, 2), 25000);
    public final static Course PRIME_02_02 = new Course(START_SEQ + 16, "Паста с брокколи", PRIME,
            LocalDate.of(2019, 8, 2), 30000);
    public final static Course PRIME_02_03 = new Course(START_SEQ + 17, "Раф", PRIME,
            LocalDate.of(2019, 8, 2), 30000);

    public final static List<Course> PRIME_01 = List.of(PRIME_01_01, PRIME_01_02, PRIME_01_03);
    public final static List<Course> PRIME_02 = List.of(PRIME_02_01, PRIME_02_02, PRIME_02_03);

    public final static Course KFC_01_01 = new Course(START_SEQ + 18, "Тёмный бургер", KFC,
            LocalDate.of(2019, 8, 1), 19900);
    public final static Course KFC_01_02 = new Course(START_SEQ + 19, "Капучино", KFC,
            LocalDate.of(2019, 8, 1), 9900);
    public final static Course KFC_02_01 = new Course(START_SEQ + 20, "Твистер острый", KFC,
            LocalDate.of(2019, 8, 2), 18000);
    public final static Course KFC_02_02 = new Course(START_SEQ + 21, "Чай в ассортименте", KFC,
            LocalDate.of(2019, 8, 2), 8900);

    public final static List<Course> KFC_01 = List.of(KFC_01_01, KFC_01_02);
    public final static List<Course> KFC_02 = List.of(KFC_02_01, KFC_02_02);

    public final static Course MUMU_01_01 = new Course(START_SEQ + 22, "Капустный салат", MUMU,
            LocalDate.of(2019, 8, 1), 5990);
    public final static Course MUMU_01_02 = new Course(START_SEQ + 23, "Борщ", MUMU,
            LocalDate.of(2019, 8, 1), 11990);
    public final static Course MUMU_01_03 = new Course(START_SEQ + 24, "Квас", MUMU,
            LocalDate.of(2019, 8, 1), 8990);
    public final static Course MUMU_02_01 = new Course(START_SEQ + 25, "Винегрет", MUMU,
            LocalDate.of(2019, 8, 2), 10080);
    public final static Course MUMU_02_02 = new Course(START_SEQ + 26, "Домашняя котлета с картофельным пюре", MUMU,
            LocalDate.of(2019, 8, 2), 15150);
    public final static Course MUMU_02_03 = new Course(START_SEQ + 27, "Американо со сливками", MUMU,
            LocalDate.of(2019, 8, 2), 12020);

    public final static List<Course> MUMU_01 = List.of(MUMU_01_01, MUMU_01_02, MUMU_01_03);
    public final static List<Course> MUMU_02 = List.of(MUMU_02_01, MUMU_02_02, MUMU_02_03);

    private CourseTestData() {
    }

    public static void assertMatch(Course actual, Course expected) {
        assertThat(actual).isEqualToIgnoringGivenFields("restaurant");
    }

    public static void assertMatch(Iterable<Course> actual, Course... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Course> actual, Iterable<Course> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }
}