package com.example.data.todoapp.rest.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.data.todoapp.rest.model.Task;
import jakarta.transaction.Transactional;

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

    @Query("""
            SELECT case when count(t) > 0 then true else false end
            FROM Task t
            WHERE t.id = :id and type(t) = :type
            """)
    boolean existsByIdAndTaskType(@Param("type") Class<? extends Task> taskType, Long id);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE check_list_item
            set checked = not checked
            WHERE task_id = :id and id = :itemId
            """, nativeQuery = true)
    int toggleCheckListItem(Long id, Long itemId);
}
