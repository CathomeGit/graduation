package ru.javawebinar.topjava.graduation.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date"}, name = "votes_unique_user_date_idx")}
)
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Vote() {
    }

    public Vote(LocalDate date, Restaurant restaurant, User user) {
        this(null, date, restaurant, user);
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant, User user) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", restaurant=" + restaurant +
                ", user=" + user +
                ", id=" + id +
                '}';
    }
}