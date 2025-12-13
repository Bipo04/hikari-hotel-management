package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomTypeAmenity;

import java.util.List;

@Repository
public interface RoomTypeAmenityRepository extends JpaRepository<RoomTypeAmenity, Long> {
    void deleteByRoomTypeId(Long roomTypeId);

    List<RoomTypeAmenity> findByRoomTypeId(Long roomTypeId);

    boolean existsByRoomTypeIdAndAmenityId(Long roomTypeId, Long amenityId);
}
