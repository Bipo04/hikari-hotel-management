package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.CustomerTierRequest;
import org.web.hikarihotelmanagement.dto.request.ReorderTierRequest;
import org.web.hikarihotelmanagement.dto.response.CustomerTierDetailResponse;
import org.web.hikarihotelmanagement.entity.CustomerTier;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.mapper.CustomerTierMapper;
import org.web.hikarihotelmanagement.repository.CustomerTierRepository;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.CustomerTierService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerTierServiceImpl implements CustomerTierService {

    private final CustomerTierRepository customerTierRepository;
    private final CustomerTierMapper customerTierMapper;
    private final UserRepository userRepository;

    @Override
    public List<CustomerTierDetailResponse> getAllTiers() {
        return customerTierRepository.findByActiveTrueOrderByTierOrderAsc()
                .stream()
                .map(customerTierMapper::toDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerTierDetailResponse getTierById(Long id) {
        CustomerTier tier = customerTierRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy hạng khách hàng với ID: " + id));
        return customerTierMapper.toDetailResponse(tier);
    }

    @Override
    @Transactional
    public CustomerTierDetailResponse createTier(CustomerTierRequest request) {
        if (customerTierRepository.findByCode(request.getCode()).isPresent()) {
            throw new ApiException("Mã hạng đã tồn tại: " + request.getCode());
        }

        List<CustomerTier> allTiers = customerTierRepository.findAllByOrderByTierOrderAsc();
        int nextOrder = allTiers.isEmpty() ? 1 : allTiers.get(allTiers.size() - 1).getTierOrder() + 1;

        CustomerTier tier = customerTierMapper.toEntity(request);
        tier.setTierOrder(nextOrder);
        CustomerTier savedTier = customerTierRepository.save(tier);

        log.info("Tạo mới hạng khách hàng: {} ({}) với thứ tự {}", savedTier.getName(), savedTier.getCode(), nextOrder);
        return customerTierMapper.toDetailResponse(savedTier);
    }

    @Override
    @Transactional
    public CustomerTierDetailResponse updateTier(Long id, CustomerTierRequest request) {
        CustomerTier existingTier = customerTierRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy hạng khách hàng với ID: " + id));

        if (!existingTier.getCode().equals(request.getCode())) {
            if (customerTierRepository.findByCode(request.getCode()).isPresent()) {
                throw new ApiException("Mã hạng đã tồn tại: " + request.getCode());
            }
        }

        customerTierMapper.updateEntityFromRequest(request, existingTier);
        CustomerTier updatedTier = customerTierRepository.save(existingTier);

        log.info("Cập nhật hạng khách hàng: {} ({})", updatedTier.getName(), updatedTier.getCode());
        return customerTierMapper.toDetailResponse(updatedTier);
    }

    @Override
    @Transactional
    public void deleteTier(Long id) {
        CustomerTier tier = customerTierRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy hạng khách hàng với ID: " + id));

        if (tier.getUsers() != null && !tier.getUsers().isEmpty()) {
            throw new ApiException("Không thể xóa hạng đang được sử dụng bởi " +
                    tier.getUsers().size() + " người dùng. Vui lòng chuyển họ sang hạng khác trước.");
        }

        Integer deletedOrder = tier.getTierOrder();

        customerTierRepository.delete(tier);
        log.info("Đã xóa hạng khách hàng: {} ({}) với thứ tự {}", tier.getName(), tier.getCode(), deletedOrder);

        List<CustomerTier> tiersAfter = customerTierRepository.findTiersAfterOrder(deletedOrder);
        for (CustomerTier t : tiersAfter) {
            int oldOrder = t.getTierOrder();
            t.setTierOrder(oldOrder - 1);
            customerTierRepository.save(t);
            log.info("Đã dồn thứ tự hạng {} từ {} xuống {}", t.getCode(), oldOrder, oldOrder - 1);
        }
    }

    @Override
    @Transactional
    public void reorderTiers(List<ReorderTierRequest.TierOrderItem> tiers) {
        if (tiers == null || tiers.isEmpty()) {
            throw new ApiException("Danh sách hạng không được để trống");
        }

        long totalTiersInDb = customerTierRepository.count();
        if (tiers.size() != totalTiersInDb) {
            throw new ApiException("Phải truyền đủ tất cả các hạng. Hiện có " + totalTiersInDb + " hạng, nhưng chỉ nhận được " + tiers.size());
        }

        List<Long> tierIds = tiers.stream().map(t -> t.getId()).toList();
        List<CustomerTier> existingTiers = customerTierRepository.findAllById(tierIds);

        if (existingTiers.size() != tierIds.size()) {
            throw new ApiException("Một số ID hạng không tồn tại");
        }

        List<Integer> orders = tiers.stream().map(t -> t.getOrder()).sorted().toList();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i) != i + 1) {
                throw new ApiException(
                        "Thứ tự phải bắt đầu từ 1 và liên tục. Mong đợi: " + (i + 1) + ", nhưng nhận: " + orders.get(i)
                );
            }
        }

        long distinctOrders = tiers.stream().map(t -> t.getOrder()).distinct().count();
        if (distinctOrders != tiers.size()) {
            throw new ApiException("Phát hiện thứ tự bị trùng lặp");
        }

        for (org.web.hikarihotelmanagement.dto.request.ReorderTierRequest.TierOrderItem item : tiers) {
            CustomerTier tier = customerTierRepository.findById(item.getId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy hạng: " + item.getId()));

            Integer oldOrder = tier.getTierOrder();
            tier.setTierOrder(item.getOrder());
            customerTierRepository.save(tier);

            log.info("Đã thay đổi thứ tự hạng {} từ {} thành {}", tier.getCode(), oldOrder, item.getOrder());
        }

        log.info("Sắp xếp lại {} hạng thành công", tiers.size());
    }

    @Override
    @Transactional
    public void updateCustomerTier(User user) {
        List<CustomerTier> eligibleTiers = customerTierRepository.findEligibleTiers(
                user.getTotalSpent(),
                user.getTotalBookings()
        );
        System.out.println("eligibleTiers: " + eligibleTiers);

        if (!eligibleTiers.isEmpty()) {
            CustomerTier newTier = eligibleTiers.get(0);

            CustomerTier currentTier = user.getCustomerTier();
            if (currentTier == null || !currentTier.getId().equals(newTier.getId())) {
                log.info("Cập nhật hạng khách hàng cho user {} từ '{}' sang '{}'",
                        user.getEmail(),
                        currentTier != null ? currentTier.getName() : "Chưa có",
                        newTier.getName());

                user.setCustomerTier(newTier);
                userRepository.save(user);
            }
        } else {
            CustomerTier defaultTier = customerTierRepository.findByCode("BRONZE")
                    .orElseThrow(() -> new ApiException("Không tìm thấy hạng mặc định BRONZE"));

            if (user.getCustomerTier() == null ||
                    !user.getCustomerTier().getId().equals(defaultTier.getId())) {

                log.info("Gán hạng mặc định BRONZE cho user {}", user.getEmail());

                user.setCustomerTier(defaultTier);
                userRepository.save(user);
            }
        }
    }

    @Override
    @Transactional
    public void updateUserStatistics(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng với ID: " + userId));

        BigDecimal currentSpent = user.getTotalSpent();
        if (currentSpent == null) {
            currentSpent = BigDecimal.ZERO;
        }
        user.setTotalSpent(currentSpent.add(amount));

        Integer currentBookings = user.getTotalBookings();
        if (currentBookings == null) {
            currentBookings = 0;
        }
        user.setTotalBookings(currentBookings + 1);

        userRepository.save(user);

        updateCustomerTier(user);

        log.info("Đã cập nhật thống kê cho user {}: Tổng chi tiêu = {}, Tổng số lần đặt = {}, Hạng hiện tại = {}",
                user.getEmail(),
                user.getTotalSpent(),
                user.getTotalBookings(),
                user.getCustomerTier() != null ? user.getCustomerTier().getName() : "Không xác định");
    }

}