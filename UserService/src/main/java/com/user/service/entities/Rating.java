package com.user.service.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
	private String ratingId;
	private Long userId;
	private String hotelId;
	private int rating;
	private String feedback;
	private Hotel hotel;
}
