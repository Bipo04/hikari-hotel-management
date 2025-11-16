package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.web.hikarihotelmanagement.entity.RoomTypeAmenity;

import java.util.List;

public interface RoomTypeAmenityRepository extends JpaRepository<RoomTypeAmenity, Long> {
    List<RoomTypeAmenity> findByRoomTypeId(Long roomTypeId);
    void deleteByRoomTypeId(Long roomTypeId);
}
