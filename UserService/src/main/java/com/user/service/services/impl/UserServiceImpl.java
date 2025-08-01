package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.entities.User;
import com.user.service.exceptions.ResourceNotFoundException;
import com.user.service.external.services.HotelService;
import com.user.service.external.services.RatingService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RatingService ratingService;
	
//	@Override
//	public User saveUser(User user) {
//		// TODO Auto-generated method stub
//		String randomUserId=UUID.randomUUID().toString();
//		user.setUserId(randomUserId);
//		return userRepository.save(user);
//	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User getUser(Long userId) {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given id is not found on server!! : "+userId));
//		Rating[] ratingsOfUser=restTemplate.getForObject("http://RATINGSERVICE/ratings/users/"+user.getUserId(), Rating[].class);
//		List<Rating> ratings=Arrays.stream(ratingsOfUser).toList();
		List<Rating> ratings=ratingService.getRatingByUserId(userId);
		List<Rating> ratingList=ratings.stream().map(rating -> {
			// api call to hotel service to get the hotel
//			Hotel hotel=restTemplate.getForObject("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
			Hotel hotel=hotelService.getHotel(rating.getHotelId());
			// set the hotel to rating
			rating.setHotel(hotel);
			// return the rating
			return rating;
		}).collect(Collectors.toList());
		
		user.setRatings(ratingList);
		return user;
	}

}
