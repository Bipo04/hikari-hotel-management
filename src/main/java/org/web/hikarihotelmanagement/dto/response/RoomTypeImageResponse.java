package org.web.hikarihotelmanagement.dto.response;

public record RoomTypeImageResponse(
        Long id,
        String imageUrl,
        Boolean isPrimary
) {}