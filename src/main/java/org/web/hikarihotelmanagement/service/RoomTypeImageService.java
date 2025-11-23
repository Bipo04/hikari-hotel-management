package org.web.hikarihotelmanagement.service;

import org.springframework.web.multipart.MultipartFile;
import org.web.hikarihotelmanagement.dto.response.RoomTypeImageResponse;

import java.util.List;

public interface RoomTypeImageService {
    List<RoomTypeImageResponse> uploadImages(Long roomTypeId, List<MultipartFile> files);
    List<RoomTypeImageResponse> updateImages(Long roomTypeId, List<MultipartFile> files);
    void deleteAllImagesByRoomType(Long roomTypeId);
    List<RoomTypeImageResponse> getImagesByRoomType(Long roomTypeId);
}
