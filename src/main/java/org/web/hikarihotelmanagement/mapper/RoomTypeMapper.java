package org.web.hikarihotelmanagement.mapper;

import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.entity.RoomTypeAmenity;

import java.util.stream.Collectors;

public class RoomTypeMapper {

    public static RoomTypeResponse toResponse(RoomType entity) {
        RoomTypeResponse dto = new RoomTypeResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setRoomClass(entity.getRoomClass() != null ? entity.getRoomClass().name() : null);
        dto.setDescription(entity.getDescription());
        dto.setCapacity(entity.getCapacity());
        dto.setPrice(entity.getPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getRoomTypeAmenities() != null) {
            dto.setAmenities(
                    entity.getRoomTypeAmenities().stream()
                            .map(RoomTypeAmenity::getAmenity)
                            .map(a -> {
                                RoomTypeResponse.AmenityResponse ar = new RoomTypeResponse.AmenityResponse();
                                ar.setId(a.getId());
                                ar.setName(a.getName());
                                ar.setDescription(a.getDescription());
                                return ar;
                            })
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
