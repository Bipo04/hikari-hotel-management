package org.web.hikarihotelmanagement.service;

import jakarta.servlet.http.HttpServletRequest;
import org.web.hikarihotelmanagement.dto.request.CreateBookingRequest;
import org.web.hikarihotelmanagement.dto.response.BookingResponse;

public interface BookingService {
    
    BookingResponse createBooking(CreateBookingRequest request, String userEmail, HttpServletRequest httpRequest);
}
