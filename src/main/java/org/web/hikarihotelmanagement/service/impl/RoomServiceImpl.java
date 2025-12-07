package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.dto.request.RoomCreateRequest;
import org.web.hikarihotelmanagement.dto.request.RoomUpdateRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.RoomRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public RoomResponse create(RoomCreateRequest req) {

        if (roomRepository.existsByRoomNumber(req.getRoomNumber())) {
            throw new ApiException("Room number đã tồn tại");
        }

        RoomType roomType = roomTypeRepository.findById(req.getRoomTypeId())
                .orElseThrow(() -> new ApiException("Không tìm thấy RoomType với id = " + req.getRoomTypeId()));

        Room room = new Room();
        room.setRoomNumber(req.getRoomNumber());
        room.setRoomType(roomType);
        room.setDescription(req.getDescription());
        room.setStatus(req.getStatus());

        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy Room với id = " + id));

        return mapToResponse(room);
    }

    @Override
    public List<RoomResponse> getAll() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse update(Long id, RoomUpdateRequest req) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy Room với id = " + id));

        if (req.getRoomNumber() != null && !req.getRoomNumber().equals(room.getRoomNumber())) {
            if (roomRepository.existsByRoomNumber(req.getRoomNumber())) {
                throw new ApiException("Room number đã tồn tại");
            }
            room.setRoomNumber(req.getRoomNumber());
        }

        if (req.getRoomTypeId() != null) {
            RoomType roomType = roomTypeRepository.findById(req.getRoomTypeId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy RoomType với id = " + req.getRoomTypeId()));
            room.setRoomType(roomType);
        }

        if (req.getDescription() != null) {
            room.setDescription(req.getDescription());
        }

        if (req.getStatus() != null) {
            room.setStatus(req.getStatus());
        }

        roomRepository.save(room);
        return mapToResponse(room);
    }
    @Override
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy Room với id = " + id));

        roomRepository.delete(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType().getId(),
                room.getRoomType().getName(), // nếu RoomType có field name
                room.getDescription(),
                room.getStatus(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
