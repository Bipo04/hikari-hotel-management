package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web.hikarihotelmanagement.dto.request.RoomTypeRequest;
import org.web.hikarihotelmanagement.entity.*;
// Sửa: Xóa import cũ không còn tồn tại
// import org.web.hikarihotelmanagement.mapper.RoomTypeMapper2;
// Giữ lại import mapper đã đổi tên
import org.web.hikarihotelmanagement.enums.RoomClass;
import org.web.hikarihotelmanagement.mapper.RoomTypeMapper;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.RoomRepository2;
import org.web.hikarihotelmanagement.repository.AmenityRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeAmenityRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository2 roomRepository2;
    private final AmenityRepository amenityRepository;
    private final RoomTypeAmenityRepository roomTypeAmenityRepository;


    // Đảm bảo tên biến này khớp với tên interface mới: RoomTypeMapper
    private final RoomTypeMapper roomTypeMapper;


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

    @Transactional
    @Override
    public RoomType updateRoomType(Long id, RoomTypeRequest request) {
        RoomType existing = roomTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với id: " + id));

        if (request.getName() != null) {
            existing.setName(request.getName());
        }

        if (request.getRoomClass() != null) {
            try {
                existing.setRoomClass(RoomClass.valueOf(request.getRoomClass().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("roomClass không hợp lệ: " + request.getRoomClass());
            }
        }

        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        if (request.getCapacity() != null) {
            existing.setCapacity(request.getCapacity());
        }

        if (request.getPrice() != null) {
            existing.setPrice(request.getPrice());
        }

        RoomType savedRoomType = roomTypeRepository.save(existing);

        if (request.getAmenityIds() != null) {
            roomTypeAmenityRepository.deleteByRoomTypeId(savedRoomType.getId());

            if (!request.getAmenityIds().isEmpty()) {
                List<Amenity> amenities = amenityRepository.findAllById(request.getAmenityIds());

                if (amenities.size() != request.getAmenityIds().size()) {
                    throw new IllegalArgumentException("Có amenityId không tồn tại");
                }

                List<RoomTypeAmenity> mappings = amenities.stream()
                        .map(a -> {
                            RoomTypeAmenity rta = new RoomTypeAmenity();
                            rta.setRoomType(savedRoomType);
                            rta.setAmenity(a);
                            return rta;
                        })
                        .toList();

                roomTypeAmenityRepository.saveAll(mappings);
            }
        }

        return savedRoomType;
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
                    AvailableRoomTypeResponse response =
                            roomTypeMapper.toAvailableRoomTypeResponse(roomType);

                    Long availableCount = roomRepository2.countAvailableRoomsByRoomType(
                            roomType.getId(),
                            request.getCheckInDate(),
                            request.getCheckOutDate().minusDays(1),
                            numberOfDays
                    );
                    response.setAvailableRoomCount(availableCount != null ? availableCount.intValue() : 0);

                    // NEW: set primary image url
                    String primaryImageUrl = roomType.getImages().stream()
                            .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                            .map(RoomTypeImage::getImageUrl)
                            .findFirst()
                            .orElse(null);
                    response.setPrimaryImageUrl(primaryImageUrl);

                    return response;
                })
                .filter(response -> response.getAvailableRoomCount() > 0)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public RoomTypeDetailResponse getRoomTypeDetailWithAvailableRooms(
            Long roomTypeId,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {

        // Nếu không truyền ngày → chỉ trả thông tin + tất cả phòng
        if (checkInDate == null || checkOutDate == null) {
            return getRoomTypeDetailWithoutDate(roomTypeId);
        }

        // Ngược lại → logic kiểm tra ngày + lọc phòng trống
        validateDates(checkInDate, checkOutDate);

        LocalDate lastNightDate = checkOutDate.minusDays(1);
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, lastNightDate) + 1;

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ApiException("Không tìm thấy loại phòng"));

        RoomTypeDetailResponse response = roomTypeMapper.toRoomTypeDetailResponse(roomType);

        List<Amenity> amenities = roomType.getRoomTypeAmenities().stream()
                .map(rta -> rta.getAmenity())
                .collect(Collectors.toList());

        response.setAmenities(roomTypeMapper.toAmenityResponseList(amenities));

        List<Room> availableRooms = roomRepository2.findAvailableRoomsByRoomType(
                roomTypeId,
                checkInDate,
                lastNightDate,
                numberOfDays
        );

        response.setAvailableRooms(roomTypeMapper.toRoomDetailResponseList(availableRooms));

        return response;
    }

    private RoomTypeDetailResponse getRoomTypeDetailWithoutDate(Long roomTypeId) {

        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ApiException("Không tìm thấy loại phòng"));

        RoomTypeDetailResponse response = roomTypeMapper.toRoomTypeDetailResponse(roomType);

        List<Amenity> amenities = roomType.getRoomTypeAmenities().stream()
                .map(rta -> rta.getAmenity())
                .collect(Collectors.toList());

        response.setAmenities(roomTypeMapper.toAmenityResponseList(amenities));

        // Lấy toàn bộ phòng, không lọc theo ngày
        List<Room> rooms = roomType.getRooms();
        response.setAvailableRooms(roomTypeMapper.toRoomDetailResponseList(rooms));

        return response;
    }


    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) return;

        LocalDate today = LocalDate.now();

        if (checkInDate.isBefore(today)) {
            throw new ApiException("Ngày check-in không thể là ngày trong quá khứ");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new ApiException("Ngày check-out phải sau ngày check-in");
        }
    }

}