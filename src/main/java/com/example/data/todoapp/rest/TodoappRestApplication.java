package com.example.data.todoapp.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class TodoappRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoappRestApplication.class, args);
	}

}
