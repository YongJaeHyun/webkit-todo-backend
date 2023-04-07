package com.example.todo.persistence;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.todo.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	UserEntity getById(String id);
	UserEntity findByEmail(String email);
	Boolean existsByEmail(String email);
	UserEntity findByEmailAndPassword(String email, String password);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update UserEntity u set username = ?1, password = ?2 where email = ?3")
	void updateUserInfo(String username, String password, String email);
}
