package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalRooms;
    private long totalUsers;
    private long ordersThisMonth;
    private BigDecimal revenueThisMonth;

    private List<RevenueStatisticItem> revenueStats;

    private List<CustomerTierStatisticItem> customerTierStats;

    private List<LatestBookingItem> latestBookings;

    private List<LatestCustomerItem> latestCustomers;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueStatisticItem {
        private String month;
        private long orderCount;
        private BigDecimal revenue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerTierStatisticItem {
        private String tier;
        private long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LatestBookingItem {
        private String code;
        private String customerName;
        private String status;
        private LocalDate createdDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LatestCustomerItem {
        private String name;
        private String email;
        private String phone;
        private LocalDate createdDate;
    }
}
