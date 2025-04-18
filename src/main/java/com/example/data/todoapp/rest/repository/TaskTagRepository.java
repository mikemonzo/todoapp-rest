package com.example.data.todoapp.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.data.todoapp.rest.model.TaskTag;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {

}
