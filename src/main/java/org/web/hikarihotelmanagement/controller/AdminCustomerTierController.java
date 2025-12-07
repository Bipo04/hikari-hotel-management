package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.CustomerTierRequest;
//import org.web.hikarihotelmanagement.dto.request.ReorderTierRequest;
import org.web.hikarihotelmanagement.dto.response.CustomerTierDetailResponse;
import org.web.hikarihotelmanagement.service.CustomerTierService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/customer-tiers")
@RequiredArgsConstructor
@Tag(name = "Admin - Customer Tier", description = "API quản lý hạng khách hàng (Admin)")
@SecurityRequirement(name = "bearerAuth")
public class AdminCustomerTierController {

    private final CustomerTierService customerTierService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả các hạng khách hàng")
    public ResponseEntity<List<CustomerTierDetailResponse>> getAllTiers() {
        List<CustomerTierDetailResponse> tiers = customerTierService.getAllTiers();
        return ResponseEntity.ok(tiers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết một hạng")
    public ResponseEntity<CustomerTierDetailResponse> getTierById(@PathVariable Long id) {
        CustomerTierDetailResponse tier = customerTierService.getTierById(id);
        return ResponseEntity.ok(tier);
    }

    @PostMapping
    @Operation(summary = "Tạo hạng mới")
    public ResponseEntity<CustomerTierDetailResponse> createTier(@Valid @RequestBody CustomerTierRequest request) {
        CustomerTierDetailResponse createdTier = customerTierService.createTier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTier);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin hạng")
    public ResponseEntity<CustomerTierDetailResponse> updateTier(
            @PathVariable Long id,
            @Valid @RequestBody CustomerTierRequest request
    ) {
        CustomerTierDetailResponse updatedTier = customerTierService.updateTier(id, request);
        return ResponseEntity.ok(updatedTier);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một hạng")
    public ResponseEntity<Map<String, String>> deleteTier(@PathVariable Long id) {
        customerTierService.deleteTier(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã xóa hạng thành công");
        
        return ResponseEntity.ok(response);
    }
    
//    @PutMapping("/reorder")
//    @Operation(summary = "Sắp xếp lại thứ tự các hạng",
//               description = "Cập nhật thứ tự cho nhiều tier cùng lúc dựa trên danh sách truyền vào")
//    public ResponseEntity<Map<String, String>> reorderTiers(@Valid @RequestBody ReorderTierRequest request) {
//        customerTierService.reorderTiers(request.getTiers());
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Đã sắp xếp lại thứ tự các tier thành công");
//
//        return ResponseEntity.ok(response);
//    }
}
