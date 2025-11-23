package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
// SỬA: Import RoomTypeMapper đã được đổi tên
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.service.RoomTypeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Room Type", description = "API hiển thị loại phòng")
@CrossOrigin(origins = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    // THÊM: Tiêm (inject) RoomTypeMapper vào Controller
    private final RoomTypeMapper roomTypeMapper;

    // Lấy danh sách tất cả loại phòng
    @GetMapping("/public/room-types")
    public ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes() {
        List<RoomTypeResponse> list = roomTypeService.getAllRoomTypes()
                .stream().map(roomTypeMapper::toResponse) // SỬA: Dùng đối tượng đã tiêm
                .toList();
        return ResponseEntity.ok(list);
    }

    // Lấy chi tiết 1 loại phòng theo ID
    @GetMapping("public/room-types/{id}")
    public ResponseEntity<RoomTypeResponse> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.getRoomTypeById(id)
                .map(roomType -> ResponseEntity.ok(roomTypeMapper.toResponse(roomType))) // SỬA: Dùng đối tượng đã tiêm
                .orElse(ResponseEntity.notFound().build());
    }

    // Tìm loại phòng theo tên
    @GetMapping("/public/room-types/search")
    public ResponseEntity<RoomType> getRoomTypeByName(@RequestParam String name) {
        return roomTypeService.getRoomTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo mới loại phòng
    @PostMapping("/admin/room-types")
    public ResponseEntity<?> createRoomType(@RequestBody RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.toEntity(request);
        RoomType created = roomTypeService.createRoomType(roomType);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomTypeMapper.toResponse(created));
    }

    // Cập nhật loại phòng
    @PutMapping("/admin/room-types/{id}")
    public ResponseEntity<?> updateRoomType(@PathVariable Long id, @RequestBody RoomTypeRequest request) {
        RoomType roomType = roomTypeMapper.toEntity(request);
        RoomType updated = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(roomTypeMapper.toResponse(updated));
    }

    // Xóa loại phòng
    @DeleteMapping("/admin/room-types/{id}")
    public ResponseEntity<?> deleteRoomType(@PathVariable Long id) {
        try {
            roomTypeService.deleteRoomType(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("public/room-types/available")
    @Operation(summary = "Lấy danh sách các loại phòng có phòng trống",
            description = "Trả về danh sách các loại phòng có phòng trống trong khoảng thời gian check-in và check-out")
    public ResponseEntity<List<AvailableRoomTypeResponse>> getAvailableRoomTypes(
            @Valid @RequestBody AvailableRoomTypeRequest request
    ) {
        List<AvailableRoomTypeResponse> availableRoomTypes = roomTypeService.getAvailableRoomTypes(request);
        return ResponseEntity.ok(availableRoomTypes);
    }

    @GetMapping("public/room-types/{roomTypeId}/details")
    @Operation(
            summary = "Lấy chi tiết loại phòng và danh sách phòng có thể đặt",
            description = "Trả về thông tin chi tiết của loại phòng bao gồm amenities và danh sách các phòng cụ thể có thể đặt trong khoảng thời gian (nếu truyền check-in/check-out)"
    )
    public ResponseEntity<RoomTypeDetailResponse> getRoomTypeDetailWithAvailableRooms(
            @PathVariable Long roomTypeId,

            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate
    ) {
        RoomTypeDetailResponse roomTypeDetail =
                roomTypeService.getRoomTypeDetailWithAvailableRooms(
                        roomTypeId,
                        checkInDate,
                        checkOutDate
                );

        return ResponseEntity.ok(roomTypeDetail);
    }

}