package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.graduation.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=?1")
    int delete(int id);

    @EntityGraph(value = "restaurant-with-offers-dishes", type = EntityGraph.EntityGraphType.FETCH)
    List<Restaurant> findAllByOffers_date(LocalDate date, Sort sort);
}