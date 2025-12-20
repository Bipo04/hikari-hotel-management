package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.RoomCreateRequest;
import org.web.hikarihotelmanagement.dto.request.RoomUpdateRequest;
import org.web.hikarihotelmanagement.dto.response.RoomAvailabilityBookingInfoDto;
import org.web.hikarihotelmanagement.dto.response.RoomAvailabilityCalendarResponse;
import org.web.hikarihotelmanagement.dto.response.RoomResponse;
import org.web.hikarihotelmanagement.entity.Room;
import org.web.hikarihotelmanagement.entity.RoomAvailability;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRepository;
import org.web.hikarihotelmanagement.repository.RoomRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;

    @Override
    @Transactional
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

        room = roomRepository.save(room);
        
        // Tạo 3 tháng availability cho room mới
        createAvailabilitiesForRoom(room, 3);
        log.info("Tạo room mới {} với 3 tháng availability", room.getRoomNumber());
        
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

    @Override
    @Transactional
    public void createAvailabilitiesForRoom(Room room, int months) {
        // Lấy ngày đầu tiên của tháng hiện tại
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(months);
        
        List<RoomAvailability> availabilities = new ArrayList<>();
        
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            RoomAvailability availability = new RoomAvailability();
            availability.setRoom(room);
            availability.setAvailableDate(date);
            availability.setPrice(room.getRoomType().getPrice());
            availability.setIsAvailable(true);
            availabilities.add(availability);
        }
        
        roomAvailabilityRepository.saveAll(availabilities);
        log.info("Tạo {} ngày availability cho room {} (từ ngày đầu tháng)", availabilities.size(), room.getRoomNumber());
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

    @Override
    @Transactional(readOnly = true)
    public List<RoomAvailabilityCalendarResponse> getRoomCalendar(Long roomId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from và to là bắt buộc");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from không được sau to");
        }

        roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Không tìm thấy Room với id = " + roomId));

        List<RoomAvailability> records = roomAvailabilityRepository.findByRoomAndDateRange(roomId, from, to);

        List<RoomAvailabilityBookingInfoDto> bookingInfos =
                roomAvailabilityRepository.findBookingInfoForUnavailableDates(roomId, from, to);

        Map<LocalDate, RoomAvailabilityBookingInfoDto> bookingMap = new HashMap<>();
        for (RoomAvailabilityBookingInfoDto info : bookingInfos) {
            bookingMap.putIfAbsent(info.getDate(), info);
        }

        return records.stream().map(ra -> {
            Long bookingId = null;
            String bookingCode = null;

            if (Boolean.FALSE.equals(ra.getIsAvailable())) {
                RoomAvailabilityBookingInfoDto info = bookingMap.get(ra.getAvailableDate());
                if (info != null) {
                    bookingId = info.getBookingId();
                    bookingCode = info.getBookingCode();
                }
            }

            return new RoomAvailabilityCalendarResponse(
                    ra.getAvailableDate(),
                    ra.getIsAvailable(),
                    ra.getPrice(),
                    bookingId,
                    bookingCode
            );
        }).toList();
    }
}
