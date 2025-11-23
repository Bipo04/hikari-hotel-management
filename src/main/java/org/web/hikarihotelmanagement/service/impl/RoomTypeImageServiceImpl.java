package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.web.hikarihotelmanagement.dto.response.RoomTypeImageResponse;
import org.web.hikarihotelmanagement.entity.RoomType;
import org.web.hikarihotelmanagement.entity.RoomTypeImage;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.mapper.RoomTypeImageMapper;
import org.web.hikarihotelmanagement.repository.RoomTypeImageRepository;
import org.web.hikarihotelmanagement.repository.RoomTypeRepository;
import org.web.hikarihotelmanagement.service.RoomTypeImageService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeImageServiceImpl implements RoomTypeImageService {

    private final RoomTypeImageRepository roomTypeImageRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final CloudflareR2Service cloudflareR2Service;
    private final RoomTypeImageMapper roomTypeImageMapper;

    @Override
    @Transactional
    public List<RoomTypeImageResponse> uploadImages(Long roomTypeId, List<MultipartFile> files) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ApiException("Không tìm thấy loại phòng với id: " + roomTypeId));

        // Check if images already exist
        if (roomTypeImageRepository.existsByRoomTypeId(roomTypeId)) {
            throw new ApiException("Ảnh đã tồn tại cho loại phòng này. Vui lòng sử dụng chức năng cập nhật.");
        }

        if (files == null || files.isEmpty()) {
            throw new ApiException("Ít nhất một ảnh là bắt buộc");
        }

        List<RoomTypeImage> savedImages = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            
            // Upload file to R2
            String imageKey = cloudflareR2Service.uploadFile(file);
            String imageUrl = cloudflareR2Service.getPublicUrl(imageKey);

            // Create new image entity
            RoomTypeImage image = new RoomTypeImage();
            image.setRoomType(roomType);
            image.setImageUrl(imageUrl);
            image.setImageKey(imageKey);
            // First image is primary
            image.setIsPrimary(i == 0);

            savedImages.add(roomTypeImageRepository.save(image));
        }

        log.info("Đã tải {} ảnh thành công cho loại phòng {}", savedImages.size(), roomTypeId);

        return roomTypeImageMapper.toResponseList(savedImages);
    }

    @Override
    @Transactional
    public List<RoomTypeImageResponse> updateImages(Long roomTypeId, List<MultipartFile> files) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ApiException("Không tìm thấy loại phòng với id: " + roomTypeId));

        if (files == null || files.isEmpty()) {
            throw new ApiException("Ít nhất một ảnh là bắt buộc");
        }

        // Delete all existing images
        deleteAllImagesByRoomType(roomTypeId);

        // Upload new images
        List<RoomTypeImage> savedImages = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            
            // Upload file to R2
            String imageKey = cloudflareR2Service.uploadFile(file);
            String imageUrl = cloudflareR2Service.getPublicUrl(imageKey);

            // Create new image entity
            RoomTypeImage image = new RoomTypeImage();
            image.setRoomType(roomType);
            image.setImageUrl(imageUrl);
            image.setImageKey(imageKey);
            // First image is primary
            image.setIsPrimary(i == 0);

            savedImages.add(roomTypeImageRepository.save(image));
        }

        log.info("Đã cập nhật {} ảnh thành công cho loại phòng {}", savedImages.size(), roomTypeId);

        return roomTypeImageMapper.toResponseList(savedImages);
    }

    @Override
    @Transactional
    public void deleteAllImagesByRoomType(Long roomTypeId) {
        List<RoomTypeImage> images = roomTypeImageRepository.findByRoomTypeIdOrderByIdAsc(roomTypeId);
        
        // Delete all images from R2
        images.forEach(image -> {
            try {
                cloudflareR2Service.deleteFile(image.getImageKey());
            } catch (Exception e) {
                log.error("Lỗi khi xóa ảnh từ R2: {}", image.getImageKey(), e);
            }
        });

        // Delete from database
        roomTypeImageRepository.deleteByRoomTypeId(roomTypeId);
        log.info("Đã xóa tất cả ảnh cho loại phòng {}", roomTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeImageResponse> getImagesByRoomType(Long roomTypeId) {
        List<RoomTypeImage> images = roomTypeImageRepository.findByRoomTypeIdOrderByIdAsc(roomTypeId);
        return roomTypeImageMapper.toResponseList(images);
    }
}
