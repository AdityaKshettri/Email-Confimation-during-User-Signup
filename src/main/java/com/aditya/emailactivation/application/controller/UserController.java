package com.aditya.emailactivation.application.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.emailactivation.application.entity.User;
import com.aditya.emailactivation.application.model.Mail;
import com.aditya.emailactivation.application.service.MailService;
import com.aditya.emailactivation.application.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	@GetMapping("")
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody User user, HttpServletRequest request)
	{
		System.out.println(user);
		User existingUser = userService.findByEmail(user.getEmail());
		if(existingUser != null) 
		{
			return ResponseEntity
					.badRequest()
					.body("User with this email already exists");
		}
		user.setConfirmationToken(UUID.randomUUID().toString());
		userService.save(user);
		System.out.println(user);
		
		String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		System.out.println(appUrl);
		
		Mail mail = new Mail();
		mail.setMailFrom("sender@gmail.com");
		mail.setMailTo(user.getEmail());
		mail.setMailSubject("Email Confirmation");
		mail.setMailContent("To confirm you email-address, please click the link below:\n"
				+ appUrl + "/users/confirm?token=" + user.getConfirmationToken());
		mailService.sendEmail(mail);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("User successfully signed up!");
	}
	
	@GetMapping("/confirm")
	public ResponseEntity<?> confirmEmail(@RequestParam String token)
	{
		User user = userService.findByConfirmationToken(token);
		if(user == null) 
		{
			return ResponseEntity
					.badRequest()
					.body("Invalid token");
		}
		user.setEnabled(true);
		userService.save(user);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("User email successfully verified!");
	}
}
