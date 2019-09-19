package ru.javawebinar.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "courses",
        uniqueConstraints = {@UniqueConstraint(name = "courses_unique_name_restaurant_idx",
                columnNames = {"name", "restaurant_id"})
        }
)
public class Course extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = Persist.class)
    @JsonBackReference
    private Restaurant restaurant;

    public Course() {
    }

    public Course(Course course) {
        this(course.getId(), course.getName(), course.getRestaurant());
    }

    public Course(String name, Restaurant restaurant) {
        this(null, name, restaurant);
    }

    public Course(Integer id, String name, Restaurant restaurant) {
        super(id, name);
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Course{" +
                "restaurant=" + restaurant.name +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}