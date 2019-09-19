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
        uniqueConstraints = {@UniqueConstraint(name = "offers_unique_course_restaurant_date_idx",
                columnNames = {"course_id", "restaurant_id", "date"})})
public class Offer extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull
    private Course course;

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
        this(offer.getId(), offer.getCourse(), offer.getRestaurant(), offer.getDate(), offer.getPrice());
    }

    public Offer(Course course, Restaurant restaurant, LocalDate date, Integer price) {
        this(null, course, restaurant, date, price);
    }

    public Offer(Integer id, Course course, Restaurant restaurant, LocalDate date, Integer price) {
        super(id);
        this.course = course;
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
                "course=" + course.name +
                ", restaurant=" + restaurant.name +
                ", date=" + date +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}