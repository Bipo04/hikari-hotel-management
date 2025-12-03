package org.web.hikarihotelmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_tiers")
public class CustomerTier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // BRONZE, SILVER, GOLD, DIAMOND

    @Column(nullable = false, length = 100)
    private String name; // Đồng, Bạc, Vàng, Kim Cương

    @Column(name = "min_spending", precision = 15, scale = 2, nullable = false)
    private BigDecimal minSpending;

    @Column(name = "min_bookings", nullable = false)
    private Integer minBookings;

    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;

    @Column(name = "tier_order", nullable = false, unique = true)
    private Integer tierOrder;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "customerTier")
    private List<User> users = new ArrayList<>();
}
