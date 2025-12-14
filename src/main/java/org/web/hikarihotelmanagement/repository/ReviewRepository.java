package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.Review;
import org.web.hikarihotelmanagement.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByBookingIdAndUserId(Long bookingId, Long userId);
    
    boolean existsByBookingIdAndUserId(Long bookingId, Long userId);

    List<Review> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT r FROM Review r " +
            "JOIN r.booking b " +
            "JOIN b.requests req " +
            "JOIN req.room room " +
            "WHERE room.roomType.id = :roomTypeId " +
            "ORDER BY r.createdAt DESC")
    List<Review> findByRoomTypeId(@Param("roomTypeId") Long roomTypeId);
}
