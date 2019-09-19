package ru.javawebinar.topjava.graduation.to;

import ru.javawebinar.topjava.graduation.model.Restaurant;

import java.time.LocalDate;

public class VoteResultTo {
    private LocalDate date;
    private Restaurant restaurant;
    private Long count;

    public VoteResultTo() {
    }

    public VoteResultTo(LocalDate date, Restaurant restaurant, Long count) {
        this.date = date;
        this.restaurant = restaurant;
        this.count = count;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteResultTo{" +
                "date=" + date +
                ", restaurant=" + restaurant +
                ", count=" + count +
                '}';
    }
}