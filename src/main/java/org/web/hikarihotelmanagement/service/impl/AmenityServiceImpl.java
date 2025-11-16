package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.dto.request.AmenityRequest;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;
import org.web.hikarihotelmanagement.entity.Amenity;
import org.web.hikarihotelmanagement.mapper.AmenityMapper;
import org.web.hikarihotelmanagement.repository.AmenityRepository;
import org.web.hikarihotelmanagement.service.AmenityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepo;

    @Override
    public AmenityResponse create(AmenityRequest req) {
        Amenity a = new Amenity();
        a.setName(req.getName());
        a.setDescription(req.getDescription());
        amenityRepo.save(a);
        return AmenityMapper.toResponse(a);
    }

    @Override
    public AmenityResponse update(Long id, AmenityRequest req) {
        Amenity a = amenityRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        a.setName(req.getName());
        a.setDescription(req.getDescription());
        return AmenityMapper.toResponse(a);
    }

    @Override
    public AmenityResponse getById(Long id) {
        Amenity a = amenityRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return AmenityMapper.toResponse(a);
    }

    @Override
    public List<AmenityResponse> getAll() {
        return amenityRepo.findAll().stream().map(AmenityMapper::toResponse).collect(Collectors.toList());
    }
}
