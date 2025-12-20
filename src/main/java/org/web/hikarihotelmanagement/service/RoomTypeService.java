package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface RoomTypeService {
    RoomType createRoomType(RoomType roomType);
    List<RoomType> getAllRoomTypes();
    RoomType updateRoomType(Long id, RoomTypeRequest request);
    List<AvailableRoomTypeResponse> getAvailableRoomTypes(AvailableRoomTypeRequest request);
    RoomTypeDetailResponse getRoomTypeDetailWithAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}