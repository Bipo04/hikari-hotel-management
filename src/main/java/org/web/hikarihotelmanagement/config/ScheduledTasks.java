package org.web.hikarihotelmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.entity.Booking;
import org.web.hikarihotelmanagement.entity.Request;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomAvailability;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.RequestRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRequestRepository;
import org.web.hikarihotelmanagement.repository.RoomRepository;
import org.web.hikarihotelmanagement.service.UserService;

import java.time.LocalDate;
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
    private final RoomRepository roomRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnverifiedUsersTask() {
        log.info("Chạy tác vụ định kỳ: Xóa người dùng chưa xác thực");
        userService.deleteUnverifiedUsers();
    }
    
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredBookings() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);
        
        List<Booking> expiredBookings = bookingRepository.findExpiredPendingBookings(expirationTime);
        
        if (!expiredBookings.isEmpty()) {
            log.info("Tìm thấy {} đơn dặt đã quá hạn chưa thanh toán", expiredBookings.size());
            
            for (Booking booking : expiredBookings) {
                booking.setStatus(BookingStatus.CANCELLED);
                booking.setPaymentUrl(null);
                unlockRooms(booking);
                bookingRepository.save(booking);
                log.info("Số đơn quá hạn đã hủy: {}", booking.getBookingCode());
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

    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void manageMonthlyRoomAvailabilities() {
        log.info("=== Bắt đầu tác vụ quản lý room availability hàng tháng ===");
        
        LocalDate firstDayOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        
        try {
            int deletedCount = deleteUnbookedAvailabilitiesInRange(firstDayOfLastMonth, firstDayOfThisMonth);
            log.info("Đã xóa {} availability chưa được đặt của tháng trước", deletedCount);
        } catch (Exception e) {
            log.error("Lỗi khi xóa availability cũ: {}", e.getMessage(), e);
        }
        
        List<Room> allRooms = roomRepository.findAll();
        log.info("Tìm thấy {} phòng cần tạo availability mới", allRooms.size());
        
        LocalDate startDate = LocalDate.now().plusMonths(2).withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1);
        
        int totalCreated = 0;
        for (Room room : allRooms) {
            try {
                int created = createAvailabilitiesForRoomInRange(room, startDate, endDate);
                totalCreated += created;
            } catch (Exception e) {
                log.error("Lỗi khi tạo availability cho phòng {}: {}", room.getRoomNumber(), e.getMessage());
            }
        }
        
        log.info("=== Hoàn thành: Đã tạo {} availability mới cho {} phòng ===", totalCreated, allRooms.size());
    }
    
    private int deleteUnbookedAvailabilitiesInRange(LocalDate startDate, LocalDate endDate) {
        List<RoomAvailability> toDelete = roomAvailabilityRepository.findAll().stream()
                .filter(ra -> ra.getAvailableDate().isAfter(startDate.minusDays(1)) 
                           && ra.getAvailableDate().isBefore(endDate))
                .filter(ra -> ra.getIsAvailable())
                .filter(ra -> ra.getRoomAvailabilityRequests().isEmpty())
                .toList();
        
        roomAvailabilityRepository.deleteAll(toDelete);
        return toDelete.size();
    }
    
    private int createAvailabilitiesForRoomInRange(Room room, LocalDate startDate, LocalDate endDate) {
        int created = 0;
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            // Kiểm tra xem availability đã tồn tại chưa
            if (!roomAvailabilityRepository.existsByRoomIdAndAvailableDate(room.getId(), date)) {
                RoomAvailability availability = new RoomAvailability();
                availability.setRoom(room);
                availability.setAvailableDate(date);
                availability.setPrice(room.getRoomType().getPrice());
                availability.setIsAvailable(true);
                roomAvailabilityRepository.save(availability);
                created++;
            }
        }
        
        if (created > 0) {
            log.info("Tạo {} availability cho phòng {}", created, room.getRoomNumber());
        }
        
        return created;
    }
}