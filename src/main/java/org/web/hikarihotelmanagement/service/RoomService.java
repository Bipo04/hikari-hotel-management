package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.RoomCreateRequest;
import org.web.hikarihotelmanagement.dto.request.RoomUpdateRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.dto.response.RoomAvailabilityCalendarResponse;
import org.web.hikarihotelmanagement.entity.Room;


import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomResponse create(RoomCreateRequest req);

    RoomResponse getById(Long id);

    List<RoomResponse> getAll();

    RoomResponse update(Long id, RoomUpdateRequest req);

    void delete(Long id);

    List<RoomAvailabilityCalendarResponse> getRoomCalendar(Long roomId, LocalDate from, LocalDate to);
    
    void createAvailabilitiesForRoom(Room room, int months);

}
