package com.user.service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

import com.user.service.entities.User;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;
	//create
//	@PostMapping
//	public ResponseEntity<User> createUser(@RequestBody User user){
//		User user1=userService.saveUser(user);
//		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
//	}
	//single user get
	@GetMapping("/{userId}")
	@CircuitBreaker(name="ratingHotelBreaker",fallbackMethod="ratingHotelFallback")
//	@Retry(name="ratingHotelService",fallbackMethod="ratingHotelFallback")
//	@RateLimiter(name="userRateLimiter",fallbackMethod="ratingHotelFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable Long userId){
		User user=userService.getUser(userId);
		return ResponseEntity.ok(user);
	}
	
	// creating fallback method 
	public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
		User user=User.builder().email("dummy@gmail.com").name("Dummy").
				about("Dummy user created because some service is down").userId(111L).password("password").build();	
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	//all users get
	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		List<User> allUser=userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}
}
