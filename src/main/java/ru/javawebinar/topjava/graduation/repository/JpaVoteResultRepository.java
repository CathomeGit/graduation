package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.VoteResult;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface JpaVoteResultRepository extends JpaRepository<VoteResult, Integer> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<VoteResult> findAllByDate(LocalDate date, Sort sort);
}