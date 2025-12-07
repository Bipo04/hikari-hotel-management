package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.Room;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository2 extends JpaRepository<Room, Long> {
    
    /**
     * Tìm các phòng trống theo loại phòng và khoảng thời gian
     * Dựa vào bảng room_availabilities
     */
    @Query("SELECT DISTINCT r FROM Room r " +
           "JOIN r.roomAvailabilities ra " +
           "WHERE r.roomType.id = :roomTypeId " +
           "AND r.status = 0 " + // AVAILABLE - phòng đang hoạt động
           "AND ra.availableDate BETWEEN :checkInDate AND :checkOutDate " +
           "AND ra.isAvailable = true " +
           "GROUP BY r.id " +
           "HAVING COUNT(DISTINCT ra.availableDate) = :numberOfDays")
    List<Room> findAvailableRoomsByRoomType(@Param("roomTypeId") Long roomTypeId,
                                            @Param("checkInDate") LocalDate checkInDate, 
                                            @Param("checkOutDate") LocalDate checkOutDate,
                                            @Param("numberOfDays") Long numberOfDays);
    
    /**
     * Đếm số lượng phòng trống theo loại phòng
     * Chỉ đếm những phòng có đủ tất cả các ngày trong khoảng thời gian đều available
     * Dùng subquery để đếm chính xác số phòng
     */
    @Query("SELECT COUNT(r.id) FROM Room r " +
           "WHERE r.roomType.id = :roomTypeId " +
           "AND r.status = 0 " +
           "AND r.id IN (" +
           "    SELECT ra.room.id FROM RoomAvailability ra " +
           "    WHERE ra.availableDate BETWEEN :checkInDate AND :checkOutDate " +
           "    AND ra.isAvailable = true " +
           "    GROUP BY ra.room.id " +
           "    HAVING COUNT(DISTINCT ra.availableDate) = :numberOfDays" +
           ")")
    Long countAvailableRoomsByRoomType(@Param("roomTypeId") Long roomTypeId,
                                       @Param("checkInDate") LocalDate checkInDate, 
                                       @Param("checkOutDate") LocalDate checkOutDate,
                                       @Param("numberOfDays") Long numberOfDays);
}
