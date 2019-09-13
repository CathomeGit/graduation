package ru.javawebinar.topjava.graduation.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static ru.javawebinar.topjava.graduation.model.Role.parseRoles;
import static ru.javawebinar.topjava.graduation.model.Role.rolesToBitmask;

class RoleTest {
    private static final int EMPTY = 0;
    private static final int ROLE_USER = 1;
    private static final int ROLE_ADMIN = 2;

    @Test
    void testParseRoles() {
        assert (parseRoles(EMPTY).equals(EnumSet.noneOf(Role.class)));
        assert (parseRoles(ROLE_USER).equals(EnumSet.of(Role.ROLE_USER)));
        assert (parseRoles(ROLE_ADMIN).equals(EnumSet.of(Role.ROLE_ADMIN)));
        assert (parseRoles(ROLE_ADMIN + ROLE_USER).equals(EnumSet.allOf(Role.class)));
    }

    @Test
    void testRolesToBitmask() {
        assert (rolesToBitmask(EnumSet.noneOf(Role.class)) == EMPTY);
        assert (rolesToBitmask(EnumSet.of(Role.ROLE_USER)) == ROLE_USER);
        assert (rolesToBitmask(EnumSet.of(Role.ROLE_ADMIN)) == ROLE_ADMIN);
        assert (rolesToBitmask(EnumSet.allOf(Role.class)) == ROLE_ADMIN + ROLE_USER);
    }
}