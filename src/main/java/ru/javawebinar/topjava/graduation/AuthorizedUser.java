package ru.javawebinar.topjava.graduation;

import ru.javawebinar.topjava.graduation.model.User;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;
    private int userId;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        this.userId = user.getId();
    }

    public int getId() {
        return userId;
    }
}