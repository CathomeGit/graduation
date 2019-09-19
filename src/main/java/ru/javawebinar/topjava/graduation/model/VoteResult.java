package ru.javawebinar.topjava.graduation.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "results",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "results_unique_date_restaurant_idx")}
)
public class VoteResult extends AbstractBaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "count", nullable = false)
    private Long count;

    public VoteResult() {
    }

    public VoteResult(LocalDate date, Restaurant restaurant, Long count) {
        this(null, date, restaurant, count);
    }

    public VoteResult(Integer id, LocalDate date, Restaurant restaurant, Long count) {
        super(id);
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
        return "VoteResult{" +
                "date=" + date +
                ", restaurant=" + restaurant +
                ", count=" + count +
                ", id=" + id +
                '}';
    }
}