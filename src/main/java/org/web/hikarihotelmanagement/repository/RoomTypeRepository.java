package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    Optional<RoomType> findByName(String name);

    @Override
    @EntityGraph(attributePaths = {"images"})
    List<RoomType> findAll();

    /**
     * Tìm các loại phòng có phòng trống trong khoảng thời gian
     * Dựa vào bảng room_availabilities
     */
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT DISTINCT rt FROM RoomType rt " +
            "JOIN rt.rooms r " +
            "JOIN r.roomAvailabilities ra " +
            "WHERE r.status = 0 " +
            "AND ra.availableDate BETWEEN :checkInDate AND :checkOutDate " +
            "AND ra.isAvailable = true")
    List<RoomType> findAvailableRoomTypes(@Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);
}
