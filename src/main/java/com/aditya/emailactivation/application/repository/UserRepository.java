package com.aditya.emailactivation.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.emailactivation.application.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	public User findByEmail(String email);
	public User findByConfirmationToken(String confirmationToken);
}
