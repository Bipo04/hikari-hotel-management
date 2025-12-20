package org.web.hikarihotelmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.AmenityRequest;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.service.AmenityService;

@RestController
@RequestMapping("/api/admin/amenities")
@RequiredArgsConstructor
public class AdminAmenityController {

    private final AmenityService service;

    @PostMapping
    public AmenityResponse create(@RequestBody AmenityRequest req) { return service.create(req); }

    @PutMapping("/{id}")
    public AmenityResponse update(@PathVariable Long id, @RequestBody AmenityRequest req) { return service.update(id, req); }
}
