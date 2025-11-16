package org.web.hikarihotelmanagement.mapper;

import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.entity.Amenity;

public class AmenityMapper {
    public static AmenityResponse toResponse(Amenity entity) {
        AmenityResponse dto = new AmenityResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
