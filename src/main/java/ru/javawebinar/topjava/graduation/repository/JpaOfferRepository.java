package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Offer;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface JpaOfferRepository extends JpaRepository<Offer, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Offer o WHERE o.restaurant.id=?1 AND o.date=?2")
    void deleteAllOnDate(int restaurantId, LocalDate date);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    Offer findByIdAndRestaurantId(int id, int restaurantId);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Offer> findAllByRestaurantIdAndDateBetween(int restaurantId, LocalDate startDate, LocalDate endDate, Sort sort);

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Offer> findAllByRestaurantId(int restaurantId, Sort sort);
}