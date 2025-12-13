package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;
import org.web.hikarihotelmanagement.entity.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "bookingCode", source = "booking.bookingCode")
    @Mapping(target = "userName", source = "user.name")
    ReviewResponse toReviewResponse(Review review);
}
