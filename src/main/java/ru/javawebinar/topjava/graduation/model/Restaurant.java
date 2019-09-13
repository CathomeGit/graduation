package ru.javawebinar.topjava.graduation.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_name_idx")})
public class Restaurant extends AbstractNamedEntity {

    @OneToMany(targetEntity = Course.class, fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    private List<Course> courses;

    protected Restaurant() {
    }

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

}