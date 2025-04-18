package com.example.data.todoapp.rest.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class CheckListTask extends Task {

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CheckListItem> items = new ArrayList<>();

    public void addItem(CheckListItem item) {
        items.add(item);
        item.setTask(this);
    }

    public void removeItem(CheckListItem item) {
        items.remove(item);
        item.setTask(this);
    }

    public void removeItemById(Long id) {
        items.removeIf(item -> item.getId().equals(id));
    }
}
