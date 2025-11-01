package org.web.hikarihotelmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.web.hikarihotelmanagement.enums.IdentityType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guests")
public class Guest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "identity_type")
    private IdentityType identityType;

    @Column(name = "identity_number", unique = true)
    private String identityNumber;

    @Column(name = "identity_issued_date")
    private LocalDate identityIssuedDate;

    @Column(name = "identity_issued_place")
    private String identityIssuedPlace;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
