package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.graduation.model.Dish;

import java.util.List;

@Repository
public interface JpaDishRepository extends JpaRepository<Dish, Integer> {
    @Modifying
    @Query("DELETE FROM Dish c WHERE c.id=?1 AND c.restaurant.id =?2")
    int delete(int id, int restaurantId);

    Dish findByIdAndRestaurantId(int id, int restaurantId);

    List<Dish> findAllByRestaurantId(int restaurantId, Sort sort);

    Dish getByRestaurantIdAndName(int restaurantId, String name);
}