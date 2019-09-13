package ru.javawebinar.topjava.graduation.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumSet;

public enum Role implements GrantedAuthority {
    // New elements must be added at the end with increasing code value
    ROLE_USER(1 << 0),
    ROLE_ADMIN(1 << 1);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public static EnumSet<Role> parseRoles(int bitmask) {
        EnumSet<Role> roles = EnumSet.noneOf(Role.class);
        for (Role role : values()) {
            if ((bitmask & role.getCode()) != 0) {
                roles.add(role);
            }
        }
        return roles;
    }

    public static int rolesToBitmask(EnumSet<Role> roles) {
        int bitmask = 0;
        for (Role role : roles) {
            int roleCode = role.getCode();
            bitmask |= roleCode;
        }
        return bitmask;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}