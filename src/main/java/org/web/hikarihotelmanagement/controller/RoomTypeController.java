package org.web.hikarihotelmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.service.RoomTypeService;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    // Lấy danh sách tất cả loại phòng
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes() {
        List<RoomTypeResponse> list = roomTypeService.getAllRoomTypes()
                .stream().map(RoomTypeMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    // Lấy chi tiết 1 loại phòng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.getRoomTypeById(id)
                .map(roomType -> ResponseEntity.ok(RoomTypeMapper.toResponse(roomType)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Tìm loại phòng theo tên
    @GetMapping("/search-by-name")
    public ResponseEntity<RoomType> getRoomTypeByName(@RequestParam String name) {
        return roomTypeService.getRoomTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo mới loại phòng
    @PostMapping("/create")
    public ResponseEntity<?> createRoomType(@RequestBody RoomTypeRequest request) {
        RoomType roomType = RoomTypeMapper.toEntity(request);
        RoomType created = roomTypeService.createRoomType(roomType);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoomTypeMapper.toResponse(created));
    }

    // Cập nhật loại phòng
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest request) {
        RoomType roomType = RoomTypeMapper.toEntity(request);
        RoomType updated = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(RoomTypeMapper.toResponse(updated));
    }

    // Xóa loại phòng
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable Long id) {
        try {
            roomTypeService.deleteRoomType(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
