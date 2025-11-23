package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
// THÊM CÁC IMPORT THIẾU
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
// Import các DTO/Entity hiện tại
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomDetailResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.Amenity;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    // 1. THÊM: Ánh xạ từ Request DTO sang Entity (Dùng cho POST/PUT)
    RoomType toEntity(RoomTypeRequest request);

    // 2. THÊM: Ánh xạ từ Entity sang Response DTO (Dùng cho GET)
    RoomTypeResponse toResponse(RoomType entity);

    // *************************************************************
    // Các phương thức MapStruct hiện tại (giữ nguyên)
    // *************************************************************

    AvailableRoomTypeResponse toAvailableRoomTypeResponse(RoomType roomType);

    RoomTypeDetailResponse toRoomTypeDetailResponse(RoomType roomType);

    AmenityResponse toAmenityResponse(Amenity amenity);

    List<AmenityResponse> toAmenityResponseList(List<Amenity> amenities);

    RoomDetailResponse toRoomDetailResponse(Room room);

    List<RoomDetailResponse> toRoomDetailResponseList(List<Room> rooms);
}