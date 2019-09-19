package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Vote;
import ru.javawebinar.topjava.graduation.model.VoteResult;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface JpaVoteRepository extends JpaRepository<Vote, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 AND v.date=?2")
    void deleteOnDate(int userId, LocalDate date);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    Vote findByIdAndUserId(int id, int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Vote> findAllByUserId(int userId, Sort sort);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Vote> findAllByUserIdAndDateBetween(int userId, LocalDate startDate, LocalDate endDate, Sort sort);

    @Query("SELECT new ru.javawebinar.topjava.graduation.model.VoteResult(v.date, r, COUNT(v)) FROM Restaurant r " +
            "LEFT JOIN Vote v ON v.restaurant.id = r.id AND v.date=?1 WHERE EXISTS(SELECT o FROM Offer o " +
            "WHERE r.id = o.restaurant.id AND o.date=?1)" +
            "GROUP BY v.date, r.id ORDER BY COUNT(v) DESC")
    List<VoteResult> retrieveVoteResult(LocalDate date);
}