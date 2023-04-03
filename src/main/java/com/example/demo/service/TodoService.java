package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

@Service
public class TodoService {

	@Autowired
	private TodoRepository repository;

	public String testService() {
		// Todo Entity 생성
		TodoEntity entity = TodoEntity.builder().userId("user01").title("My First todo item").build();
		// Todo Entity 저장
		repository.save(entity);
		TodoEntity savedEntity = repository.searchByUserId(entity.getUserId()).get(0);
		return savedEntity.getUserId();
	}
}
