package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.AmenityRequest;
import org.web.hikarihotelmanagement.dto.response.AmenityResponse;

import java.util.List;

public interface AmenityService {
    AmenityResponse create(AmenityRequest req);
    AmenityResponse update(Long id, AmenityRequest req);
    AmenityResponse getById(Long id);
    List<AmenityResponse> getAll();
}
