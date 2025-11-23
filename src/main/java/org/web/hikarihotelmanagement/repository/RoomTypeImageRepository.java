package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomTypeImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeImageRepository extends JpaRepository<RoomTypeImage, Long> {
    
    List<RoomTypeImage> findByRoomTypeIdOrderByIdAsc(Long roomTypeId);
    
    Optional<RoomTypeImage> findByRoomTypeIdAndIsPrimaryTrue(Long roomTypeId);
    
    void deleteByRoomTypeId(Long roomTypeId);
    
    boolean existsByRoomTypeId(Long roomTypeId);
}
