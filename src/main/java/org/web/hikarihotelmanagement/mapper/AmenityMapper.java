package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.entity.Amenity;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
    AmenityResponse toResponse(Amenity entity);
}