package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.web.hikarihotelmanagement.dto.response.RoomTypeImageResponse;
import org.web.hikarihotelmanagement.service.impl.RoomTypeImageServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/admin/room-types/{roomTypeId}/images")
@RequiredArgsConstructor
@Tag(name = "Room Type Images Management", description = "APIs for managing room type images")
@SecurityRequirement(name = "bearerAuth")
public class AdminRoomTypeImageController {

    private final RoomTypeImageServiceImpl roomTypeImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Đăng ảnh của loại phòng",
               description = "Đăng nhiều ảnh cho 1 loại phòng khi chưa có ảnh. Ảnh đầu tiên sẽ là primary.")
    public ResponseEntity<List<RoomTypeImageResponse>> uploadImages(
            @PathVariable Long roomTypeId,
            @Parameter(description = "Ảnh", required = true)
            @RequestPart("files") List<MultipartFile> files) {
        
        List<RoomTypeImageResponse> responses = roomTypeImageService.uploadImages(roomTypeId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật ảnh cho loại phòng",
               description = "Xóa các ảnh cũ và cập nhật ảnh mới. Ảnh đầu tiên là primary")
    public ResponseEntity<List<RoomTypeImageResponse>> updateImages(
            @PathVariable Long roomTypeId,
            @Parameter(description = "Ảnh", required = true)
            @RequestPart("files") List<MultipartFile> files) {
        
        List<RoomTypeImageResponse> responses = roomTypeImageService.updateImages(roomTypeId, files);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả các ảnh của 1 loại phòng")
    public ResponseEntity<List<RoomTypeImageResponse>> getImages(@PathVariable Long roomTypeId) {
        List<RoomTypeImageResponse> images = roomTypeImageService.getImagesByRoomType(roomTypeId);
        return ResponseEntity.ok(images);
    }
}
