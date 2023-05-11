package com.stefan.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stefan.springjwt.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

  User findByEmail(String email);
  
}
