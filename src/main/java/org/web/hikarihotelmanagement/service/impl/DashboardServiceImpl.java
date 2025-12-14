package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.response.DashboardResponse;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.Role;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.CustomerTierRepository;
import org.web.hikarihotelmanagement.repository.RoomRepository;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.DashboardService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CustomerTierRepository customerTierRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        DashboardResponse res = new DashboardResponse();

        // ===== 4 cards =====
        res.setTotalRooms(roomRepository.count());
        res.setTotalUsers(userRepository.countByRole(Role.USER));

        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        long ordersThisMonth = bookingRepository.countByCreatedAtBetweenAndStatus(
                startOfMonth, endOfMonth, BookingStatus.PAYMENT_COMPLETED
        );
        res.setOrdersThisMonth(ordersThisMonth);

        BigDecimal revenueThisMonth = bookingRepository.sumAmountByCreatedAtBetweenAndStatus(
                startOfMonth, endOfMonth, BookingStatus.PAYMENT_COMPLETED
        );
        res.setRevenueThisMonth(revenueThisMonth != null ? revenueThisMonth : BigDecimal.ZERO);

        // ===== Revenue chart: 3 months =====
        List<DashboardResponse.RevenueStatisticItem> revenueStats = new ArrayList<>();
        YearMonth threeMonthsAgo = currentMonth.minusMonths(2);

        for (int i = 0; i < 3; i++) {
            YearMonth ym = threeMonthsAgo.plusMonths(i);
            LocalDateTime from = ym.atDay(1).atStartOfDay();
            LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

            long orderCount = bookingRepository.countByCreatedAtBetweenAndStatus(
                    from, to, BookingStatus.PAYMENT_COMPLETED
            );

            BigDecimal revenue = bookingRepository.sumAmountByCreatedAtBetweenAndStatus(
                    from, to, BookingStatus.PAYMENT_COMPLETED
            );
            if (revenue == null) revenue = BigDecimal.ZERO;

            revenueStats.add(new DashboardResponse.RevenueStatisticItem(
                    ym.getMonth().name().substring(0, 3),
                    orderCount,
                    revenue
            ));
        }
        res.setRevenueStats(revenueStats);

        // ===== Tier chart =====
        var tierDtos = customerTierRepository.countUsersByTierIncludeZero(Role.USER);

        res.setCustomerTierStats(
                tierDtos.stream()
                        .map(x -> new DashboardResponse.CustomerTierStatisticItem(x.getTier(), x.getCount()))
                        .toList()
        );


        // ===== Latest bookings =====
        var latestBookingsEntities = bookingRepository.findTop5ByOrderByCreatedAtDesc();

        List<DashboardResponse.LatestBookingItem> latestBookings = latestBookingsEntities.stream()
                .map(b -> new DashboardResponse.LatestBookingItem(
                        b.getBookingCode(),
                        b.getUser() != null ? b.getUser().getName() : null,
                        b.getStatus() != null ? b.getStatus().name() : null,
                        b.getCreatedAt() != null ? b.getCreatedAt().toLocalDate() : null
                ))
                .toList();
        res.setLatestBookings(latestBookings);

        // ===== Latest customers =====
        var latestCustomersEntities = userRepository.findTop5ByRoleOrderByCreatedAtDesc(Role.USER);

        List<DashboardResponse.LatestCustomerItem> latestCustomers = latestCustomersEntities.stream()
                .map(u -> new DashboardResponse.LatestCustomerItem(
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getCreatedAt() != null ? u.getCreatedAt().toLocalDate() : null
                ))
                .toList();
        res.setLatestCustomers(latestCustomers);

        return res;
    }
}
