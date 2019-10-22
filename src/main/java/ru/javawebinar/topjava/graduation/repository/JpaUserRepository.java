package ru.javawebinar.topjava.graduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.graduation.model.User;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=?1")
    int delete(int id);

    User getByEmail(String email);
}