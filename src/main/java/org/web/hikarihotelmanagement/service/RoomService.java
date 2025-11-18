package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.RoomRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse create(RoomRequest req);
    RoomResponse update(Long id, RoomRequest req);
    RoomResponse getById(Long id);
    List<RoomResponse> getAll();
}
