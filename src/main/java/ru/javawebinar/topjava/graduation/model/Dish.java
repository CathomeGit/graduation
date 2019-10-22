package ru.javawebinar.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes",
        uniqueConstraints = {@UniqueConstraint(name = "dishes_unique_name_restaurant_idx",
                columnNames = {"name", "restaurant_id"})
        }
)
public class Dish extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = Persist.class)
    @JsonBackReference
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getRestaurant());
    }

    public Dish(String name, Restaurant restaurant) {
        this(null, name, restaurant);
    }

    public Dish(Integer id, String name, Restaurant restaurant) {
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
        return "Dish{" +
                "restaurant=" + restaurant.name +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}