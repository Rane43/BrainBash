package com.prometheus.brainbash.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prometheus.brainbash.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
