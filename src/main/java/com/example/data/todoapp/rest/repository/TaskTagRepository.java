package com.example.data.todoapp.rest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.data.todoapp.rest.model.TaskTag;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {

    Optional<TaskTag> findByName(String name);
}
