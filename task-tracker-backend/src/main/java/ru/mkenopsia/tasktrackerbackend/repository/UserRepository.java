package ru.mkenopsia.tasktrackerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mkenopsia.tasktrackerbackend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
