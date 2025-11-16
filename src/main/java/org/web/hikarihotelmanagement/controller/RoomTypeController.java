package org.web.hikarihotelmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.service.RoomTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService service;

    @PostMapping
    public RoomTypeResponse create(@RequestBody RoomTypeRequest req) { return service.create(req); }

    @PutMapping("/{id}")
    public RoomTypeResponse update(@PathVariable Long id, @RequestBody RoomTypeRequest req) { return service.update(id, req); }

    @GetMapping("/{id}")
    public RoomTypeResponse detail(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<RoomTypeResponse> list() { return service.getAll(); }



    @DeleteMapping("/{roomTypeId}/amenities/{amenityId}")
    public RoomTypeResponse removeAmenity(@PathVariable Long roomTypeId, @PathVariable Long amenityId) {
        return service.removeAmenity(roomTypeId, amenityId);
    }
    //thêm
    @PostMapping("/{roomTypeId}/amenities")
    public ResponseEntity<RoomTypeResponse> addAmenitiesToRoomType(
            @PathVariable Long roomTypeId,
            @RequestBody List<Long> amenityIds
    ) {
        return ResponseEntity.ok(service.addAmenities(roomTypeId, amenityIds));
    }
}
