package com.example.todo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.model.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {

}
