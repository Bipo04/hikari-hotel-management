package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.CreateReviewRequest;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest request, String userEmail);

    List<ReviewResponse> getReviewsByUser(String userEmail);

    List<ReviewResponse> getReviewsByRoomType(Long roomTypeId);
}
