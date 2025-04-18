package com.example.data.todoapp.rest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.data.todoapp.rest.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
