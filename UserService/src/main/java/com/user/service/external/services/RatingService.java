package com.user.service.external.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.user.service.entities.Rating;

@FeignClient(name="RATINGSERVICE")
public interface RatingService {
	@GetMapping("/ratings/users/{userId}")
	List<Rating> getRatingByUserId(@PathVariable Long userId);
}
