package org.web.hikarihotelmanagement.mapper;

import org.web.hikarihotelmanagement.dto.request.RoomRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.enums.RoomStatus;

public class RoomMapper {

    public static RoomResponse toResponse(Room entity) {
        RoomResponse dto = new RoomResponse();
        dto.setId(entity.getId());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setRoomTypeName(entity.getRoomType() != null ? entity.getRoomType().getName() : null);
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public static Room toEntity(RoomRequest req, RoomType roomType) {
        Room room = new Room();
        room.setRoomNumber(req.getRoomNumber());
        room.setRoomType(roomType);
        room.setDescription(req.getDescription());
        if (req.getStatus() != null) {
            room.setStatus(RoomStatus.valueOf(req.getStatus()));
        }
        return room;
    }
}
