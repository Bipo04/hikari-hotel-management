package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomTypeService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    public RoomType createRoomType(RoomType roomType) {
        Optional<RoomType> existing = roomTypeRepository.findByName(roomType.getName());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Tên loại phòng này đã tồn tại");
        }
        return roomTypeRepository.save(roomType);
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Optional<RoomType> getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public RoomType updateRoomType(Long id, RoomType roomTypeDetails) {
        RoomType existing = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với id: " + id));

        existing.setName(roomTypeDetails.getName());
        existing.setRoomClass(roomTypeDetails.getRoomClass());
        existing.setDescription(roomTypeDetails.getDescription());
        existing.setCapacity(roomTypeDetails.getCapacity());
        existing.setPrice(roomTypeDetails.getPrice());

        return roomTypeRepository.save(existing);
    }

    @Override
    public void deleteRoomType(Long id) {
        if (!roomTypeRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy phòng với id: " + id);
        }
        roomTypeRepository.deleteById(id);
    }

    @Override
    public Optional<RoomType> getRoomTypeByName(String name) {
        return roomTypeRepository.findByName(name);
    }
}
