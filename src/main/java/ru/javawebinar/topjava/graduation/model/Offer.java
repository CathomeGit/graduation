package ru.javawebinar.topjava.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.javawebinar.topjava.graduation.web.json.PriceDeserializer;
import ru.javawebinar.topjava.graduation.web.json.PriceSerializer;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "offers",
        uniqueConstraints = {@UniqueConstraint(name = "offers_unique_dish_restaurant_date_idx",
                columnNames = {"dish_id", "restaurant_id", "date"})})
public class Offer extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    @NotNull
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(groups = Persist.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonBackReference("restaurant_offers")
    private Restaurant restaurant;

    @Column(name = "date", nullable = false, columnDefinition = "date default now()")
    @NotNull(groups = Persist.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(1)
    @JsonSerialize(using = PriceSerializer.class)
    @JsonDeserialize(using = PriceDeserializer.class)
    private Integer price;

    public Offer() {
    }

    public Offer(Offer offer) {
        this(offer.getId(), offer.getDish(), offer.getRestaurant(), offer.getDate(), offer.getPrice());
    }

    public Offer(Dish dish, Restaurant restaurant, LocalDate date, Integer price) {
        this(null, dish, restaurant, date, price);
    }

    public Offer(Integer id, Dish dish, Restaurant restaurant, LocalDate date, Integer price) {
        super(id);
        this.dish = dish;
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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

    @Override
    public String toString() {
        return "Offer{" +
                "dish=" + dish.name +
                ", restaurant=" + restaurant.name +
                ", date=" + date +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}