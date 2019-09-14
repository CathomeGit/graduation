package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Offer;

@Repository
@Transactional(readOnly = true)
public interface CrudOfferRepository extends JpaRepository<Offer, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Offer c WHERE c.id=?1")
    int delete(int id);

    @EntityGraph(attributePaths = {"course"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c FROM Offer c WHERE c.id=?1")
    Offer getWithCourse(int id);
}