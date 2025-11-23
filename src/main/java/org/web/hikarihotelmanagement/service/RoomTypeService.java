package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomTypeService {
    RoomType createRoomType(RoomType roomType);
    List<RoomType> getAllRoomTypes();
    Optional<RoomType> getRoomTypeById(Long id);
    RoomType updateRoomType(Long id, RoomType roomTypeDetails);
    void deleteRoomType(Long id);
    Optional<RoomType> getRoomTypeByName(String name);
    List<AvailableRoomTypeResponse> getAvailableRoomTypes(AvailableRoomTypeRequest request);
    RoomTypeDetailResponse getRoomTypeDetailWithAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}