package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.AvailableRoomTypeRequest;
import org.web.hikarihotelmanagement.dto.response.AvailableRoomTypeResponse;
import org.web.hikarihotelmanagement.dto.response.RoomTypeDetailResponse;
import org.web.hikarihotelmanagement.entity.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomTypeService {

    // Tạo mới một loại phòng
    RoomType createRoomType(RoomType roomType);

    // Lấy danh sách tất cả loại phòng
    List<RoomType> getAllRoomTypes();

    // Lấy chi tiết một loại phòng theo ID
    Optional<RoomType> getRoomTypeById(Long id);

    // Cập nhật thông tin loại phòng
    RoomType updateRoomType(Long id, RoomType roomTypeDetails);

    // Xóa loại phòng theo ID
    void deleteRoomType(Long id);

    //Tìm theo tên loại phòng
    Optional<RoomType> getRoomTypeByName(String name);
    
    /**
     * Lấy danh sách các loại phòng có phòng trống trong khoảng thời gian
     */
    List<AvailableRoomTypeResponse> getAvailableRoomTypes(AvailableRoomTypeRequest request);
    
    /**
     * Lấy chi tiết loại phòng bao gồm amenities và danh sách phòng có thể đặt
     */
    RoomTypeDetailResponse getRoomTypeDetailWithAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate);
}