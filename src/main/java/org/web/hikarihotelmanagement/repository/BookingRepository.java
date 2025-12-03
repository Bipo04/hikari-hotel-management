package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingCode(String bookingCode);
    
    @Query("SELECT b FROM Booking b WHERE b.status = 0 AND b.createdAt < :expirationTime")
    List<Booking> findExpiredPendingBookings(@Param("expirationTime") LocalDateTime expirationTime);
    
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
}
