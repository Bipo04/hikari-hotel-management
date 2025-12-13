package org.web.hikarihotelmanagement.service;

import jakarta.servlet.http.HttpServletRequest;
import org.web.hikarihotelmanagement.dto.request.CheckInRequest;
import org.web.hikarihotelmanagement.dto.request.CreateBookingRequest;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request, String userEmail, HttpServletRequest httpRequest);
    
    List<BookingDetailResponse> getUserBookings(String userEmail);
    
    BookingDetailResponse getBookingDetail(Long bookingId, String userEmail);
    
    // Admin methods
    List<BookingDetailResponse> getAllBookings();
    
    List<BookingDetailResponse> getBookingsByUserId(Long userId);
    
    BookingDetailResponse getBookingDetailAdmin(Long bookingId);
    
    void cancelBooking(Long bookingId);
    
    void checkInRequest(CheckInRequest request);
    
    void checkOutRequest(Long requestId);
}
