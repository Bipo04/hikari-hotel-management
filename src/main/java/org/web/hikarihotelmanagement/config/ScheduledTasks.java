package org.web.hikarihotelmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web.hikarihotelmanagement.service.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final UserService userService;

    /**
     * Tự động xóa các user chưa xác thực sau 1 ngày
     * Chạy mỗi ngày vào lúc 0 giờ sáng
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteUnverifiedUsersTask() {
        log.info("Chạy tác vụ định kỳ: Xóa người dùng chưa xác thực");
        userService.deleteUnverifiedUsers();
    }
}
