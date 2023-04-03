package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {
	List<TodoEntity> searchByUserId(String userId);
}
