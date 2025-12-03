package org.web.hikarihotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.web.hikarihotelmanagement.entity.CustomerTier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerTierRepository extends JpaRepository<CustomerTier, Long> {
    
    Optional<CustomerTier> findByCode(String code);
    
    Optional<CustomerTier> findByTierOrder(Integer tierOrder);
    
    List<CustomerTier> findByActiveTrue();
    
    List<CustomerTier> findByActiveTrueOrderByTierOrderAsc();
    
    @Query("SELECT ct FROM CustomerTier ct WHERE ct.active = true " +
           "AND (:totalSpent >= ct.minSpending OR :totalBookings >= ct.minBookings) " +
           "ORDER BY ct.tierOrder DESC")
    List<CustomerTier> findEligibleTiers(BigDecimal totalSpent, Integer totalBookings);
    
    List<CustomerTier> findAllByOrderByTierOrderAsc();
    
    @Query("SELECT ct FROM CustomerTier ct WHERE ct.tierOrder > :tierOrder ORDER BY ct.tierOrder ASC")
    List<CustomerTier> findTiersAfterOrder(Integer tierOrder);
}
