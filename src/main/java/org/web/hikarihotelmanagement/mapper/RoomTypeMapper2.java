package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomDetailResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.Amenity;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper2 {

    AvailableRoomTypeResponse toAvailableRoomTypeResponse(RoomType roomType);

    RoomTypeDetailResponse toRoomTypeDetailResponse(RoomType roomType);

    AmenityResponse toAmenityResponse(Amenity amenity);

    List<AmenityResponse> toAmenityResponseList(List<Amenity> amenities);

    RoomDetailResponse toRoomDetailResponse(Room room);

    List<RoomDetailResponse> toRoomDetailResponseList(List<Room> rooms);
}