package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.RoomAvailabilityRequest;

import java.util.List;

@Repository
public interface RoomAvailabilityRequestRepository extends JpaRepository<RoomAvailabilityRequest, Long> {
    void deleteByRequestId(Long requestId);
    
    List<RoomAvailabilityRequest> findByRequestId(Long requestId);
}
