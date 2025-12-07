package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.*;
import org.web.hikarihotelmanagement.dto.request.RoomRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    // ENTITY -> RESPONSE
    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    RoomResponse toResponse(Room room);

    // REQUEST -> ENTITY (create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomType", source = "roomTypeId", qualifiedByName = "mapRoomType")
    @Mapping(target = "roomAvailabilities", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Room toEntity(RoomRequest dto, @Context RoomTypeRepository roomTypeRepo);

    // REQUEST -> ENTITY (update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roomType", source = "roomTypeId", qualifiedByName = "mapRoomType")
    @Mapping(target = "roomAvailabilities", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Room room, RoomRequest dto,
                      @Context RoomTypeRepository roomTypeRepo);

    @Named("mapRoomType")
    default RoomType mapRoomType(Long roomTypeId, @Context RoomTypeRepository roomTypeRepo) {
        if (roomTypeId == null) return null;
        return roomTypeRepo.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("RoomType not found: " + roomTypeId));
    }
}
