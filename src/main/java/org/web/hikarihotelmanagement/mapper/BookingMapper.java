package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.entity.Booking;
import org.web.hikarihotelmanagement.entity.Guest;
import org.web.hikarihotelmanagement.entity.Request;
import org.web.hikarihotelmanagement.enums.RequestStatus;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    
    @Mapping(target = "roomType", expression = "java(getRoomTypeName(booking))")
    @Mapping(target = "image", expression = "java(getRoomTypeImage(booking))")
    @Mapping(target = "requests", expression = "java(mapRequests(booking))")
    @Mapping(target = "price", source = "amount")
    @Mapping(target = "bookingDate", source = "createdAt")
    BookingDetailResponse toBookingDetailResponse(Booking booking);
    
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    @Mapping(target = "guests", source = "numberOfGuests")
    @Mapping(target = "status", expression = "java(request.getStatus().name())")
    @Mapping(target = "guestList", expression = "java(mapGuests(request))")
    BookingDetailResponse.RequestItemResponse toRequestItemResponse(Request request);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "identityType", source = "identityType")
    @Mapping(target = "identityNumber", source = "identityNumber")
    @Mapping(target = "identityIssuedDate", source = "identityIssuedDate")
    @Mapping(target = "identityIssuedPlace", source = "identityIssuedPlace")
    BookingDetailResponse.GuestResponse toGuestResponse(Guest guest);
    
    default String getRoomTypeName(Booking booking) {
        if (booking.getRequests() != null && !booking.getRequests().isEmpty()) {
            return booking.getRequests().get(0).getRoom().getRoomType().getName();
        }
        return null;
    }
    
    default String getRoomTypeImage(Booking booking) {
        if (booking.getRequests() != null && !booking.getRequests().isEmpty()) {
            var roomType = booking.getRequests().get(0).getRoom().getRoomType();
            if (roomType.getImages() != null && !roomType.getImages().isEmpty()) {
                return roomType.getImages().get(0).getImageUrl();
            }
        }
        return null;
    }
    
    default java.util.List<BookingDetailResponse.RequestItemResponse> mapRequests(Booking booking) {
        if (booking.getRequests() == null) {
            return java.util.Collections.emptyList();
        }
        return booking.getRequests().stream()
                .map(this::toRequestItemResponse)
                .collect(java.util.stream.Collectors.toList());
    }
    
    default java.util.List<BookingDetailResponse.GuestResponse> mapGuests(Request request) {
        // Chỉ trả về danh sách guest nếu request đã check-in hoặc check-out
        if (request.getStatus() == RequestStatus.CHECKED_IN || 
            request.getStatus() == RequestStatus.CHECKED_OUT) {
            if (request.getGuests() != null && !request.getGuests().isEmpty()) {
                return request.getGuests().stream()
                        .map(this::toGuestResponse)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        return null;
    }
}
