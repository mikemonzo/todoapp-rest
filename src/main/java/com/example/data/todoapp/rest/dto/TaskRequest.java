package com.example.data.todoapp.rest.dto;

import java.util.List;

public record TaskRequest(String title, String description, List<String> items, String username) {

}
