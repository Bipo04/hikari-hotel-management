package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.service.RoomTypeService;

@RestController
@RequestMapping("/api/admin/room-types")
@RequiredArgsConstructor
@Tag(name = "Admin Room Type", description = "API admin quản lý loại phòng")
@SecurityRequirement(name = "bearerAuth")
public class AdminRoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeMapper roomTypeMapper;

    // Tạo mới loại phòng (ADMIN)
    @PostMapping
    public ResponseEntity<RoomTypeResponse> createRoomType(@RequestBody RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.toEntity(request);
        RoomType created = roomTypeService.createRoomType(roomType);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomTypeMapper.toResponse(created));
    }

    // Cập nhật loại phòng (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> updateRoomType(
            @PathVariable Long id,
            @RequestBody RoomTypeRequest request
    ) {
        RoomType roomType = roomTypeMapper.toEntity(request);
        RoomType updated = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(roomTypeMapper.toResponse(updated));
    }

}
