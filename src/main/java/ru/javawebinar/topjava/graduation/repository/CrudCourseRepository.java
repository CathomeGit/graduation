package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Course;

@Repository
@Transactional(readOnly = true)
public interface CrudCourseRepository extends JpaRepository<Course, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Course c WHERE c.id=?1")
    int delete(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c FROM Course c WHERE c.id=?1")
    Course getWithRestaurant(int id);
}