package com.aditya.emailactivation.application.service;

import java.util.List;

import com.aditya.emailactivation.application.entity.User;

public interface UserService 
{
	public List<User> findAll();
	public User findByEmail(String email);
	public User findByConfirmationToken(String confirmationToken);
	public void save(User user);
}
