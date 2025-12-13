package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.entity.RoomTypeAmenity;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.service.RoomTypeService;
import org.web.hikarihotelmanagement.repository.RoomTypeAmenityRepository;


import java.util.List;

@RestController
@RequestMapping("/api/admin/room-types")
@RequiredArgsConstructor
@Tag(name = "Admin Room Type", description = "API admin quản lý loại phòng")
@SecurityRequirement(name = "bearerAuth")
public class AdminRoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomTypeAmenityRepository roomTypeAmenityRepository;


    // Tạo mới loại phòng (ADMIN)
    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(@RequestBody RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.toEntity(request);
        RoomType created = roomTypeService.createRoomType(roomType);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomTypeMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDetailResponse> updateRoomType(
            @PathVariable Long id,
            @RequestBody RoomTypeRequest request
    ) {
        RoomType updated = roomTypeService.updateRoomType(id, request);

        RoomTypeDetailResponse res = roomTypeMapper.toRoomTypeDetailResponse(updated);

        List<RoomTypeAmenity> links = roomTypeAmenityRepository.findByRoomTypeId(updated.getId());

        List<AmenityResponse> amenities = links.stream()
                .map(RoomTypeAmenity::getAmenity)
                .map(roomTypeMapper::toAmenityResponse)
                .toList();

        res.setAmenities(amenities);

        res.setAvailableRooms(List.of());

        return ResponseEntity.ok(res);
    }



}
