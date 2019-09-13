package ru.javawebinar.topjava.graduation.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "courses",
        uniqueConstraints = {@UniqueConstraint(name = "courses_unique_name_restaurant_date_idx",
                columnNames = {"name", "restaurant_id", "date"})
        }
)
public class Course extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "date default now()")
    @NotNull
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(0)
    private Integer price;

    public Course() {
    }

    public Course(Course course) {
        this(course.getId(), course.getName(), course.getRestaurant(), course.getDate(), course.getPrice());
    }

    public Course(String name, Restaurant restaurant, LocalDate date, Integer price) {
        this(null, name, restaurant, date, price);
    }

    public Course(Integer id, String name, Restaurant restaurant, LocalDate date, Integer price) {
        super(id, name);
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}