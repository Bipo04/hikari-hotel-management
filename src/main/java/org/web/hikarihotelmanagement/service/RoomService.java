package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.RoomCreateRequest;
import org.web.hikarihotelmanagement.dto.request.RoomUpdateRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {

    RoomResponse create(RoomCreateRequest req);

    RoomResponse getById(Long id);

    List<RoomResponse> getAll();

    RoomResponse update(Long id, RoomUpdateRequest req);

    void delete(Long id);
}
