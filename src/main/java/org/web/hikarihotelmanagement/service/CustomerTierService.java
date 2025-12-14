package org.web.hikarihotelmanagement.service;

import org.web.hikarihotelmanagement.dto.request.CustomerTierRequest;
import org.web.hikarihotelmanagement.dto.response.CustomerTierDetailResponse;
import org.web.hikarihotelmanagement.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerTierService {
    List<CustomerTierDetailResponse> getAllTiers();
    
    List<CustomerTierDetailResponse> getAllActiveTiers();
    
    CustomerTierDetailResponse getTierById(Long id);
    
    CustomerTierDetailResponse getCurrentUserTier(String userEmail);
    CustomerTierDetailResponse createTier(CustomerTierRequest request);
    CustomerTierDetailResponse updateTier(Long id, CustomerTierRequest request);
    void deleteTier(Long id);
    void reorderTiers(List<org.web.hikarihotelmanagement.dto.request.ReorderTierRequest.TierOrderItem> tiers);
    void updateCustomerTier(User user);
    void updateUserStatistics(Long userId, BigDecimal amount);
}
