package org.web.hikarihotelmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.entity.Booking;
import org.web.hikarihotelmanagement.entity.Request;
import org.web.hikarihotelmanagement.entity.RoomAvailability;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.RequestRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRequestRepository;
import org.web.hikarihotelmanagement.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final RoomAvailabilityRequestRepository roomAvailabilityRequestRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnverifiedUsersTask() {
        log.info("Chạy tác vụ định kỳ: Xóa người dùng chưa xác thực");
        userService.deleteUnverifiedUsers();
    }
    
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cancelExpiredBookings() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);
        
        List<Booking> expiredBookings = bookingRepository.findExpiredPendingBookings(expirationTime);
        
        if (!expiredBookings.isEmpty()) {
            log.info("Found {} expired bookings to cancel", expiredBookings.size());
            
            for (Booking booking : expiredBookings) {
                booking.setStatus(BookingStatus.CANCELLED);
                unlockRooms(booking);
                bookingRepository.save(booking);
                log.info("Cancelled expired booking: {}", booking.getBookingCode());
            }
        }
    }
    
    private void unlockRooms(Booking booking) {
        List<Request> requests = requestRepository.findByBookingId(booking.getId());
        
        for (Request req : requests) {
            req.setStatus(RequestStatus.CANCELLED);
            
            roomAvailabilityRequestRepository.deleteByRequestId(req.getId());
            
            List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomAndDateRange(
                req.getRoom().getId(),
                req.getCheckIn().toLocalDate(),
                req.getCheckOut().toLocalDate().minusDays(1)
            );
            
            for (RoomAvailability availability : availabilities) {
                availability.setIsAvailable(true);
            }
            
            roomAvailabilityRepository.saveAll(availabilities);
        }
        
        requestRepository.saveAll(requests);
    }
}