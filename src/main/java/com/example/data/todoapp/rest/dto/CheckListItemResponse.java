package com.example.data.todoapp.rest.dto;

import com.example.data.todoapp.rest.model.CheckListItem;

public record CheckListItemResponse(
    Long id,
    String text,
    boolean checked
) {

    public static CheckListItemResponse of (CheckListItem item) {
        return new CheckListItemResponse(item.getId(), item.getText(), item.isChecked());
    }
}
