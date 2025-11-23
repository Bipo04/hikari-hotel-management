package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.web.hikarihotelmanagement.dto.response.RoomTypeImageResponse;
import org.web.hikarihotelmanagement.entity.RoomTypeImage;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeImageMapper {
    
    RoomTypeImageResponse toResponse(RoomTypeImage image);
    
    List<RoomTypeImageResponse> toResponseList(List<RoomTypeImage> images);
}
