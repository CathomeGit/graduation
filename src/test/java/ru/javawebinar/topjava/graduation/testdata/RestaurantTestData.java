package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Restaurant;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public final static Restaurant PRIME = new Restaurant(START_SEQ + 3, "Prime");
    public final static Restaurant KFC = new Restaurant(START_SEQ + 4, "KFC");
    public final static Restaurant MUMU = new Restaurant(START_SEQ + 5, "Му-Му");

    private RestaurantTestData() {
    }

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "offers", "votes");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("offers", "votes").isEqualTo(expected);
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(PRIME);
        updated.setName("Обновлено");
        return updated;
    }

    public static Restaurant getCreated() {
        return new Restaurant(null, "Ресторан");
    }
}