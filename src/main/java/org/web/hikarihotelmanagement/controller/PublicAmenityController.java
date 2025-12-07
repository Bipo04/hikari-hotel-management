package org.web.hikarihotelmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.AmenityRequest;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.service.AmenityService;

import java.util.List;

@RestController
@RequestMapping("/api/public/amenities")
@RequiredArgsConstructor
public class PublicAmenityController {

    private final AmenityService service;

    @GetMapping("/{id}")
    public AmenityResponse get(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<AmenityResponse> list() { return service.getAll(); }
}
