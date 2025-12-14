package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<RoomAvailabilityCalendarResponse> getRoomCalendar(Long roomId, LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from và to là bắt buộc");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from không được sau to");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ApiException("Không tìm thấy Room với id = " + roomId));

        BigDecimal defaultPrice = (room.getRoomType() != null ? room.getRoomType().getPrice() : null);

        // availability records
        List<RoomAvailability> records = roomAvailabilityRepository.findByRoomAndDateRange(roomId, from, to);

        Map<LocalDate, RoomAvailability> availabilityMap = new HashMap<>();
        for (RoomAvailability ra : records) {
            availabilityMap.put(ra.getAvailableDate(), ra);
        }

        // booking info cho ngày unavailable
        List<RoomAvailabilityBookingInfoDto> bookingInfos =
                roomAvailabilityRepository.findBookingInfoForUnavailableDates(roomId, from, to);

        Map<LocalDate, RoomAvailabilityBookingInfoDto> bookingMap = new HashMap<>();
        for (RoomAvailabilityBookingInfoDto info : bookingInfos) {
            // nếu trùng ngày, giữ cái đầu tiên (hoặc ghi đè tuỳ bạn)
            bookingMap.putIfAbsent(info.getDate(), info);
        }

        List<RoomAvailabilityCalendarResponse> result = new ArrayList<>();
        LocalDate d = from;

        while (!d.isAfter(to)) {
            RoomAvailability ra = availabilityMap.get(d);

            Boolean isAvailable = (ra != null ? ra.getIsAvailable() : Boolean.TRUE);
            BigDecimal price = (ra != null && ra.getPrice() != null) ? ra.getPrice() : defaultPrice;

            Long bookingId = null;
            String bookingCode = null;

            if (Boolean.FALSE.equals(isAvailable)) {
                RoomAvailabilityBookingInfoDto info = bookingMap.get(d);
                if (info != null) {
                    bookingId = info.getBookingId();
                    bookingCode = info.getBookingCode();
                }
            }

            result.add(new RoomAvailabilityCalendarResponse(d, isAvailable, price, bookingId, bookingCode));
            d = d.plusDays(1);
        }

        return result;
    }
}
