package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.graduation.model.Course;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface JpaCourseRepository extends JpaRepository<Course, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Course c WHERE c.id=?1 AND c.restaurant.id =?2")
    int delete(int id, int restaurantId);

    Course findByIdAndRestaurantId(int id, int restaurantId);

    List<Course> findAllByRestaurantId(int restaurantId, Sort sort);

    Course getByRestaurantIdAndName(int restaurantId, String name);
}