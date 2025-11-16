package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.RoomTypeResponse;
import org.web.hikarihotelmanagement.entity.Amenity;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.entity.RoomTypeAmenity;
import org.web.hikarihotelmanagement.enums.RoomClass;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.repository.AmenityRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeAmenityRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomTypeService;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepo;
    private final AmenityRepository amenityRepo;
    private final RoomTypeAmenityRepository rtaRepo;

    @Override
    public RoomTypeResponse create(RoomTypeRequest req) {
        RoomType rt = new RoomType();
        rt.setName(req.getName());
        rt.setDescription(req.getDescription());
        rt.setCapacity(req.getCapacity());
        rt.setPrice(req.getPrice());
        if (req.getRoomClass() != null) rt.setRoomClass(RoomClass.valueOf(req.getRoomClass()));
        roomTypeRepo.save(rt);
        return RoomTypeMapper.toResponse(rt);
    }

    @Override
    public RoomTypeResponse update(Long id, RoomTypeRequest req) {
        RoomType rt = roomTypeRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        rt.setName(req.getName());
        rt.setDescription(req.getDescription());
        rt.setCapacity(req.getCapacity());
        rt.setPrice(req.getPrice());
        if (req.getRoomClass() != null) rt.setRoomClass(RoomClass.valueOf(req.getRoomClass()));
        return RoomTypeMapper.toResponse(rt);
    }

    @Override
    public RoomTypeResponse getById(Long id) {
        RoomType rt = roomTypeRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return RoomTypeMapper.toResponse(rt);
    }

    @Override
    public List<RoomTypeResponse> getAll() {
        return roomTypeRepo.findAll().stream().map(RoomTypeMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoomTypeResponse addAmenities(Long roomTypeId, List<Long> amenityIds) {

        RoomType roomType = roomTypeRepo.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        List<Amenity> amenities = amenityRepo.findAllById(amenityIds);

        if (amenities.isEmpty()) {
            throw new RuntimeException("No valid amenities found");
        }

        // Xóa các quan hệ cũ để tránh trùng lặp
        roomType.getRoomTypeAmenities().clear();

        // Thêm mới quan hệ RoomTypeAmenity
        for (Amenity amenity : amenities) {
            RoomTypeAmenity rta = new RoomTypeAmenity();
            rta.setRoomType(roomType);
            rta.setAmenity(amenity);
            roomType.getRoomTypeAmenities().add(rta);
        }

        roomTypeRepo.save(roomType);

        return RoomTypeMapper.toResponse(roomType);
    }


    @Override
    @Transactional
    public RoomTypeResponse removeAmenity(Long roomTypeId, Long amenityId) {
        List<RoomTypeAmenity> list = rtaRepo.findByRoomTypeId(roomTypeId);
        for (RoomTypeAmenity rta : list) {
            if (rta.getAmenity().getId().equals(amenityId)) rtaRepo.delete(rta);
        }
        RoomType rt = roomTypeRepo.findById(roomTypeId).orElseThrow(() -> new RuntimeException("Not found"));
        return RoomTypeMapper.toResponse(rt);
    }
}
