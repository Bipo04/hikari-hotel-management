package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.RoomRequest;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.mapper.RoomMapper;
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
    @Transactional
    public RoomResponse create(RoomRequest req) {
        RoomType roomType = roomTypeRepository.findById(req.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        Room room = RoomMapper.toEntity(req, roomType);
        roomRepository.save(room);
        return RoomMapper.toResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse update(Long id, RoomRequest req) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (req.getRoomNumber() != null) room.setRoomNumber(req.getRoomNumber());
        if (req.getDescription() != null) room.setDescription(req.getDescription());
        if (req.getRoomTypeId() != null) {
            RoomType roomType = roomTypeRepository.findById(req.getRoomTypeId())
                    .orElseThrow(() -> new RuntimeException("RoomType not found"));
            room.setRoomType(roomType);
        }
        if (req.getStatus() != null) room.setStatus(org.web.hikarihotelmanagement.enums.RoomStatus.valueOf(req.getStatus()));
        roomRepository.save(room);
        return RoomMapper.toResponse(room);
    }

    @Override
    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return RoomMapper.toResponse(room);
    }

    @Override
    public List<RoomResponse> getAll() {
        return roomRepository.findAll().stream()
                .map(RoomMapper::toResponse)
                .collect(Collectors.toList());
    }
}
