package org.web.hikarihotelmanagement.mapper;

import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.enums.RoomClass;

public interface RoomTypeMapper {

    public static RoomType toEntity(RoomTypeRequest request) {
        RoomType roomType = new RoomType();
        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setCapacity(request.getCapacity());
        roomType.setPrice(request.getPrice());

        if (request.getRoomClass() != null && !request.getRoomClass().isEmpty()) {
            try {
                roomType.setRoomClass(RoomClass.valueOf(request.getRoomClass().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Giá trị roomClass không hợp lệ: " + request.getRoomClass());
            }
        }

        return roomType;
    }

    public static RoomTypeResponse toResponse(RoomType entity) {
        RoomTypeResponse response = new RoomTypeResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCapacity(entity.getCapacity());
        response.setPrice(entity.getPrice());

        if (entity.getRoomClass() != null) {
            response.setRoomClass(entity.getRoomClass().name());
        }

        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        return response;
    }
}
