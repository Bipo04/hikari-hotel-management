package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.CreateReviewRequest;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest request, String userEmail);
}
