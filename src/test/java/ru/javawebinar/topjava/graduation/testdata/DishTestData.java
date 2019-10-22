package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Dish;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.graduation.testdata.RestaurantTestData.*;

public class DishTestData {
    public final static Dish PRIME_01_SALAD = new Dish(START_SEQ + 12, "Салат с тунцом", PRIME);
    public final static Dish PRIME_01_MAIN = new Dish(START_SEQ + 13, "Лапша со свининой", PRIME);
    public final static Dish PRIME_01_DRINK = new Dish(START_SEQ + 14, "Матча латте", PRIME);
    public final static Dish PRIME_02_SALAD = new Dish(START_SEQ + 15, "Салат цезарь", PRIME);
    public final static Dish PRIME_02_MAIN = new Dish(START_SEQ + 16, "Паста с брокколи", PRIME);
    public final static Dish PRIME_02_DRINK = new Dish(START_SEQ + 17, "Раф", PRIME);

    public final static List<Dish> PRIME_DISHES = List.of(PRIME_01_MAIN, PRIME_01_DRINK, PRIME_02_MAIN,
            PRIME_02_DRINK, PRIME_01_SALAD, PRIME_02_SALAD);

    public static final Dish KFC_01_MAIN = new Dish(START_SEQ + 18, "Тёмный бургер", KFC);
    public static final Dish KFC_01_DRINK = new Dish(START_SEQ + 19, "Капучино", KFC);
    public static final Dish KFC_02_MAIN = new Dish(START_SEQ + 20, "Твистер острый", KFC);
    public static final Dish KFC_02_DRINK = new Dish(START_SEQ + 21, "Чай в ассортименте", KFC);

    public final static List<Dish> KFC_DISHES = List.of(KFC_01_DRINK, KFC_02_MAIN, KFC_01_MAIN, KFC_02_DRINK);

    public static final Dish MUMU_01_SALAD = new Dish(START_SEQ + 22, "Капустный салат", MUMU);
    public static final Dish MUMU_01_MAIN = new Dish(START_SEQ + 23, "Борщ", MUMU);
    public static final Dish MUMU_01_DRINK = new Dish(START_SEQ + 24, "Квас", MUMU);
    public static final Dish MUMU_02_SALAD = new Dish(START_SEQ + 25, "Винегрет", MUMU);
    public static final Dish MUMU_02_MAIN = new Dish(START_SEQ + 26, "Домашняя котлета с картофельным пюре", MUMU);
    public static final Dish MUMU_02_DRINK = new Dish(START_SEQ + 27, "Американо со сливками", MUMU);

    private DishTestData() {
    }

    public static void assertMatch(Dish actual, Dish expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "restaurant");
    }

    public static void assertMatch(Iterable<Dish> actual, Dish... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Dish> actual, Iterable<Dish> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant").isEqualTo(expected);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(PRIME_01_SALAD);
        updated.setName("Обновлено");
        return updated;
    }

    public static Dish getCreated() {
        return new Dish("Щи", PRIME);
    }
}