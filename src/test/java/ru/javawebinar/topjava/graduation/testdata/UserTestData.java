package ru.javawebinar.topjava.graduation.testdata;

import ru.javawebinar.topjava.graduation.model.Role;
import ru.javawebinar.topjava.graduation.model.User;
import ru.javawebinar.topjava.graduation.web.json.JsonUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.graduation.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public final static User USER1 = new User(START_SEQ, "Jane Doe", "janedoe@mail.ru", "password", Role.ROLE_USER);
    public final static User USER2 = new User(START_SEQ + 1, "Иван Иванов", "ivan-i@ya.ru", "qwerty", Role.ROLE_USER);
    public final static User ADMIN = new User(START_SEQ + 2, "John Doe", "admin@gmail.com", "admin", Role.ROLE_ADMIN, Role.ROLE_USER);

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "votes", "password");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "votes", "password").isEqualTo(expected);
    }

    public static User getUpdated() {
        User updated = new User(USER1);
        updated.setName("Обновлено");
        return updated;
    }

    public static User getCreated() {
        return new User(null, "суперюзер", "super@mail.ru", "12345", Role.ROLE_USER, Role.ROLE_ADMIN);
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}