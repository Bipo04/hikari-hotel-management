package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeImageResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.service.RoomTypeImageService;
import org.web.hikarihotelmanagement.service.RoomTypeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public/room-types")
@RequiredArgsConstructor
@Tag(name = "Public Room Type", description = "API public hiển thị loại phòng")
public class PublicRoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomTypeImageService roomTypeImageService;

    // Lấy danh sách tất cả loại phòng (PUBLIC)
    @GetMapping
    public ResponseEntity<List<RoomTypeResponse>> getAllRoomTypes() {
        List<RoomTypeResponse> list = roomTypeService.getAllRoomTypes()
                .stream()
                .map(roomTypeMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }


    // Lấy danh sách các loại phòng có phòng trống (PUBLIC)
    @GetMapping("/available")
    @Operation(
            summary = "Lấy danh sách các loại phòng có phòng trống",
            description = "Trả về danh sách các loại phòng có phòng trống trong khoảng thời gian check-in và check-out"
    )
    public ResponseEntity<List<AvailableRoomTypeResponse>> getAvailableRoomTypes(
            @Valid @ModelAttribute AvailableRoomTypeRequest request
    ) {
        List<AvailableRoomTypeResponse> availableRoomTypes =
                roomTypeService.getAvailableRoomTypes(request);
        return ResponseEntity.ok(availableRoomTypes);
    }


    // Lấy chi tiết loại phòng và danh sách phòng có thể đặt (PUBLIC)
    @GetMapping("/{roomTypeId}/details")
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

    @GetMapping("/{roomTypeId}/images")
    @Operation(summary = "Lấy tất cả các ảnh của 1 loại phòng")
    public ResponseEntity<List<RoomTypeImageResponse>> getImages(@PathVariable Long roomTypeId) {
        List<RoomTypeImageResponse> images = roomTypeImageService.getImagesByRoomType(roomTypeId);
        return ResponseEntity.ok(images);
    }
}
