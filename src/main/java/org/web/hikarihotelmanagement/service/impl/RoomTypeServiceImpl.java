package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper2;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomTypeService;import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.Amenity;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.repository.RoomRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RoomTypeMapper roomTypeMapper;
    private final RoomTypeMapper2 roomTypeMapper2;

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
    
    @Override
    @Transactional(readOnly = true)
    public List<AvailableRoomTypeResponse> getAvailableRoomTypes(AvailableRoomTypeRequest request) {
        validateDates(request.getCheckInDate(), request.getCheckOutDate());
        
        long numberOfDays = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        
        List<RoomType> availableRoomTypes = roomTypeRepository.findAvailableRoomTypes(
            request.getCheckInDate(), 
            request.getCheckOutDate().minusDays(1)
        );
        
        return availableRoomTypes.stream()
            .map(roomType -> {
                AvailableRoomTypeResponse response = roomTypeMapper2.toAvailableRoomTypeResponse(roomType);
                
                Long availableCount = roomRepository.countAvailableRoomsByRoomType(
                    roomType.getId(),
                    request.getCheckInDate(),
                    request.getCheckOutDate().minusDays(1),
                    numberOfDays
                );
                response.setAvailableRoomCount(availableCount != null ? availableCount.intValue() : 0);
                
                return response;
            })
            .filter(response -> response.getAvailableRoomCount() > 0)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoomTypeDetailResponse getRoomTypeDetailWithAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        validateDates(checkInDate, checkOutDate);
        
        LocalDate lastNightDate = checkOutDate.minusDays(1);
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, lastNightDate) + 1;

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new ApiException("Không tìm thấy loại phòng"));
        
        RoomTypeDetailResponse response = roomTypeMapper2.toRoomTypeDetailResponse(roomType);
        
        List<Amenity> amenities = roomType.getRoomTypeAmenities().stream()
            .map(rta -> rta.getAmenity())
            .collect(Collectors.toList());
        response.setAmenities(roomTypeMapper2.toAmenityResponseList(amenities));
        
        List<Room> availableRooms = roomRepository.findAvailableRoomsByRoomType(
            roomTypeId,
            checkInDate,
            lastNightDate,
            numberOfDays
        );
        response.setAvailableRooms(roomTypeMapper2.toRoomDetailResponseList(availableRooms));
        
        return response;
    }
    
    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate today = LocalDate.now();
        
        if (checkInDate.isBefore(today)) {
            throw new ApiException("Ngày check-in không thể là ngày trong quá khứ");
        }
        
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new ApiException("Ngày check-out phải sau ngày check-in");
        }
    }
}
