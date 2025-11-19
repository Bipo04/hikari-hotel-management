package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomAvailability;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {
    
    /**
     * Kiểm tra xem phòng có available trong TẤT CẢ các ngày trong khoảng thời gian không
     * Trả về true nếu TẤT CẢ các ngày đều available, false nếu có bất kỳ ngày nào không available
     */
    @Query("SELECT CASE WHEN COUNT(ra) = :numberOfDays THEN true ELSE false END " +
           "FROM RoomAvailability ra " +
           "WHERE ra.room.id = :roomId " +
           "AND ra.availableDate BETWEEN :checkInDate AND :checkOutDate " +
           "AND ra.isAvailable = true")
    Boolean isRoomFullyAvailable(@Param("roomId") Long roomId,
                                  @Param("checkInDate") LocalDate checkInDate,
                                  @Param("checkOutDate") LocalDate checkOutDate,
                                  @Param("numberOfDays") Long numberOfDays);
    
    /**
     * Lấy danh sách room availabilities cho 1 phòng trong khoảng thời gian
     */
    @Query("SELECT ra FROM RoomAvailability ra " +
           "WHERE ra.room.id = :roomId " +
           "AND ra.availableDate BETWEEN :checkInDate AND :checkOutDate " +
           "ORDER BY ra.availableDate")
    List<RoomAvailability> findByRoomAndDateRange(@Param("roomId") Long roomId,
                                                   @Param("checkInDate") LocalDate checkInDate,
                                                   @Param("checkOutDate") LocalDate checkOutDate);
}
