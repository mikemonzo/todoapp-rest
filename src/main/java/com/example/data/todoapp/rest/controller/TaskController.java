package com.example.data.todoapp.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.data.todoapp.rest.dto.EditBasicTaskRequest;
import com.example.data.todoapp.rest.dto.TaskRequest;
import com.example.data.todoapp.rest.dto.TaskResponse;
import com.example.data.todoapp.rest.model.BasicTask;
import com.example.data.todoapp.rest.model.CheckListItem;
import com.example.data.todoapp.rest.model.CheckListTask;
import com.example.data.todoapp.rest.model.User;
import com.example.data.todoapp.rest.model.Task;
import com.example.data.todoapp.rest.repository.TaskRepository;
import com.example.data.todoapp.rest.repository.TaskTagRepository;
import com.example.data.todoapp.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/tasks/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskTagRepository taskTagRepository;

    @PostMapping("/new/basic")
    public ResponseEntity<TaskResponse> newBasicTask(@RequestBody TaskRequest taskRequest) {

        Optional<User> owner = userRepository.findByUsername(taskRequest.username());

        BasicTask task = BasicTask.builder()
                .title(taskRequest.title() != null ? taskRequest.title() : "Untitled")
                .description(taskRequest.description()).owner(owner.orElse(null)).build();

        task = taskRepository.save(task);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/tasks/{id}")
                .build(task.getId());

        return ResponseEntity.created(uri).body(TaskResponse.of(task));
    }

    @PostMapping("/new/checklist")
    public ResponseEntity<TaskResponse> newCheckListTask(@RequestBody TaskRequest taskRequest) {

        Optional<User> owner = userRepository.findByUsername(taskRequest.username());

        CheckListTask task = CheckListTask.builder()
                .title(taskRequest.title() != null ? taskRequest.title() : "Untitled")
                .owner(owner.orElse(null)).build();

        taskRequest.items().stream().map(text -> CheckListItem.builder().text(text).build())
                .forEach(task::addItem);

        task = taskRepository.save(task);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/tasks/{id}")
                .build(task.getId());

        return ResponseEntity.created(uri).body(TaskResponse.of(task));
    }

    @GetMapping("/")
    public Page<TaskResponse> getAll(
            @PageableDefault(page = 0, size = 5, sort = "createdAt") Pageable pageable) {
        Page<Task> result = taskRepository.findAllWithItemsAndTags(pageable);

        if (result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found");
        return result.map(TaskResponse::of);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskRepository.findByIdWithItemsAndTags(id).map(TaskResponse::of).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @PutMapping("/basic/{id}")
    public ResponseEntity<TaskResponse> editBasicTask(
            @RequestBody EditBasicTaskRequest editBasicTaskRequest, @PathVariable Long id) {

        if (!taskRepository.existsByIdAndTaskType(BasicTask.class, id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with ID %d not found".formatted(id));
        }
        return ResponseEntity.of(
                taskRepository.findByIdWithItemsAndTags(id).map(BasicTask.class::cast).map(task -> {
                    task.setTitle(editBasicTaskRequest.title());
                    task.setDescription(editBasicTaskRequest.description());
                    return taskRepository.save(task);
                }).map(TaskResponse::of));
    }

    @PutMapping("/checklist/{id}/add/{item}")
    public ResponseEntity<TaskResponse> addItemToCheckList(@PathVariable String item,
            @PathVariable Long id) {

        if (!taskRepository.existsByIdAndTaskType(CheckListTask.class, id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with ID %d not found".formatted(id));
        }
        return ResponseEntity.of(taskRepository.findByIdWithItemsAndTags(id)
                .map(CheckListTask.class::cast).map(task -> {
                    task.addItem(CheckListItem.builder().text(item).build());
                    return taskRepository.save(task);
                }).map(TaskResponse::of));
    }

    @DeleteMapping("/checklist/{id}/del/{itemId}")
    public ResponseEntity<TaskResponse> deleteItemFromCheckList(@PathVariable("itemId") Long itemId,
            @PathVariable Long id) {

        if (!taskRepository.existsByIdAndTaskType(CheckListTask.class, id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with ID %d not found".formatted(id));
        }
        return ResponseEntity.of(taskRepository.findByIdWithItemsAndTags(id)
                .map(CheckListTask.class::cast).map(task -> {
                    task.removeItemById(itemId);
                    return taskRepository.save(task);
                }).map(TaskResponse::of));
    }

    @PutMapping("/checklist/{id}/toggle/{itemId}")
    public ResponseEntity<TaskResponse> toggleItemCheckList(@PathVariable("itemId") Long itemId,
            @PathVariable Long id) {

        if (!taskRepository.existsByIdAndTaskType(CheckListTask.class, id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with ID %d not found".formatted(id));
        }
        taskRepository.toggleCheckListItem(id, itemId);
        return ResponseEntity.of(taskRepository.findByIdWithItemsAndTags(id).map(TaskResponse::of));
    }
}

