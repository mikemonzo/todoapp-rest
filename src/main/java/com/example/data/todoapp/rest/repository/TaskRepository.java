package com.example.data.todoapp.rest.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.data.todoapp.rest.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t FROM Task t
            left join fetch t.items
            left join fetch t.tags
            """)
    Page<Task> findAllWithItemsAndTags(Pageable pageable);

    @Query("""
            SELECT t FROM Task t
            left join fetch t.items
            left join fetch t.tags
            WHERE t.id = :id
            """)
    Optional<Task> findByIdWithItemsAndTags(Long id);
}
