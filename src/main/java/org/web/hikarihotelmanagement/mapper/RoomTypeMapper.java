package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.*;
import org.web.hikarihotelmanagement.entity.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    RoomType toEntity(RoomTypeRequest request);

    @Mapping(
            target = "primaryImageUrl",
            expression = "java(entity.getImages() == null ? null : entity.getImages().stream()" +
                    ".filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))" +
                    ".map(img -> img.getImageUrl())" +
                    ".findFirst().orElse(null))"
    )
    RoomTypeResponse toResponse(RoomType entity);

    @Mapping(
            target = "primaryImageUrl",
            expression = "java(roomType.getImages() == null ? null : roomType.getImages().stream()" +
                    ".filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))" +
                    ".map(img -> img.getImageUrl())" +
                    ".findFirst().orElse(null))"
    )
    AvailableRoomTypeResponse toAvailableRoomTypeResponse(RoomType roomType);

    RoomTypeDetailResponse toRoomTypeDetailResponse(RoomType roomType);

    AmenityResponse toAmenityResponse(Amenity amenity);

    List<AmenityResponse> toAmenityResponseList(List<Amenity> amenities);

    RoomDetailResponse toRoomDetailResponse(Room room);

    List<RoomDetailResponse> toRoomDetailResponseList(List<Room> rooms);
}
