package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.CreateReviewRequest;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;
import org.web.hikarihotelmanagement.entity.Booking;
import org.web.hikarihotelmanagement.entity.Request;
import org.web.hikarihotelmanagement.entity.Review;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.ReviewRepository;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.ReviewService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final org.web.hikarihotelmanagement.mapper.ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng"));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ApiException("Không tìm thấy đơn đặt phòng"));

        // Kiểm tra booking có thuộc về user này không
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ApiException("Bạn không có quyền đánh giá đơn đặt này");
        }

        // Kiểm tra tất cả các request đã checkout chưa
        boolean allCheckedOut = booking.getRequests().stream()
                .allMatch(req -> req.getStatus() == RequestStatus.CHECKED_OUT);

        if (!allCheckedOut) {
            throw new ApiException("Chỉ có thể đánh giá khi tất cả phòng đã checkout");
        }

        // Kiểm tra đã đánh giá chưa
        if (reviewRepository.existsByBookingIdAndUserId(booking.getId(), user.getId())) {
            throw new ApiException("Bạn đã đánh giá đơn đặt phòng này rồi");
        }

        // Tạo review
        Review review = new Review();
        review.setUser(user);
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        review = reviewRepository.save(review);

        log.info("User {} đã đánh giá booking {} với {} sao", 
                user.getEmail(), booking.getBookingCode(), request.getRating());

        return reviewMapper.toReviewResponse(review);
    }
}
