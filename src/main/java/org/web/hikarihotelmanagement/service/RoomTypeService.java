package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;

import java.util.List;

public interface RoomTypeService {
    RoomTypeResponse create(RoomTypeRequest req);
    RoomTypeResponse update(Long id, RoomTypeRequest req);
    RoomTypeResponse getById(Long id);
    List<RoomTypeResponse> getAll();

    RoomTypeResponse removeAmenity(Long roomTypeId, Long amenityId);
    RoomTypeResponse addAmenities(Long roomTypeId, List<Long> amenityIds);

}
