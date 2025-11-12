-- ===============================================
-- HIKARI HOTEL MANAGEMENT - SEED DATA
-- Database: HikariHotel
-- ===============================================

-- Tạo database nếu chưa có
USE HikariHotel;

-- Xóa dữ liệu cũ (theo thứ tự foreign key)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE room_availability_requests;
TRUNCATE TABLE reviews;
TRUNCATE TABLE guests;
TRUNCATE TABLE requests;
TRUNCATE TABLE bookings;
TRUNCATE TABLE room_availabilities;
TRUNCATE TABLE room_type_amenities;
TRUNCATE TABLE rooms;
TRUNCATE TABLE room_types;
TRUNCATE TABLE amenities;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================================
-- 1. USERS (6 users: 1 admin + 5 regular users)
-- Password: password123
-- ===============================================
INSERT INTO users (email, password, name, phone, birth_date, role, status, is_verified, created_at, updated_at) VALUES
('admin@hikari.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Admin Hikari', '0901234567', '1990-01-01', 1, 1, 1, NOW(), NOW()),
('user1@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Nguyễn Văn A', '0912345678', '1995-05-15', 0, 1, 1, NOW(), NOW()),
('user2@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Trần Thị B', '0923456789', '1992-08-20', 0, 1, 1, NOW(), NOW()),
('user3@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Lê Văn C', '0934567890', '1998-03-10', 0, 1, 1, NOW(), NOW()),
('user4@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Phạm Thị D', '0945678901', '1994-11-25', 0, 1, 1, NOW(), NOW()),
('user5@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Hoàng Văn E', '0956789012', '1996-07-30', 0, 1, 1, NOW(), NOW());

-- ===============================================
-- 2. AMENITIES (10 amenities)
-- ===============================================
INSERT INTO amenities (name, description, created_at, updated_at) VALUES
('WiFi', 'Miễn phí WiFi tốc độ cao', NOW(), NOW()),
('TV', 'TV màn hình phẳng 43 inch', NOW(), NOW()),
('Điều hòa', 'Điều hòa 2 chiều inverter', NOW(), NOW()),
('Tủ lạnh', 'Tủ lạnh mini bar', NOW(), NOW()),
('Két sắt', 'Két sắt điện tử', NOW(), NOW()),
('Bồn tắm', 'Bồn tắm cao cấp', NOW(), NOW()),
('Ban công', 'Ban công view biển/thành phố', NOW(), NOW()),
('Máy sấy tóc', 'Máy sấy tóc Panasonic', NOW(), NOW()),
('Máy pha cà phê', 'Máy pha cà phê Nespresso', NOW(), NOW()),
('Dép đi trong phòng', 'Dép cao su chống trượt', NOW(), NOW());

-- ===============================================
-- 3. ROOM TYPES (6 loại phòng)
-- class: 0=Standard, 1=Superior, 2=Business, 3=Suite
-- ===============================================
INSERT INTO room_types (name, class, description, capacity, price, created_at, updated_at) VALUES
('Phòng đơn Standard', 0, 'Phòng cơ bản cho 1 người', 1, 500.00, NOW(), NOW()),
('Phòng đôi Standard', 0, 'Phòng cơ bản cho 2 người', 2, 800.00, NOW(), NOW()),
('Phòng đơn Superior', 1, 'Phòng cao cấp 1 giường', 1, 700.00, NOW(), NOW()),
('Phòng đôi Superior', 1, 'Phòng cao cấp 2 giường', 2, 1000.00, NOW(), NOW()),
('Phòng đôi Business', 2, 'Dành cho khách công tác', 2, 1200.00, NOW(), NOW()),
('Suite gia đình', 3, 'Phòng sang trọng, nhiều tiện nghi', 4, 2500.00, NOW(), NOW());

-- ===============================================
-- ===============================================
-- 4. ROOMS (48 phòng: 8 phòng đơn Standard, 8 phòng đôi Standard, 8 phòng đơn Superior, 8 phòng đôi Superior, 8 phòng Business, 8 Suite)
-- ===============================================

-- 8 Phòng đơn Standard (101-108) - room_type_id = 1
INSERT INTO rooms (room_number, room_type_id, description, status, created_at, updated_at) VALUES
('101', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('102', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('103', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('104', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('105', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('106', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('107', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('108', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),

-- 8 Phòng đôi Standard (201-208) - room_type_id = 2
('201', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('202', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('203', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('204', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('205', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('206', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('207', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('208', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),

-- 8 Phòng đơn Superior (301-308) - room_type_id = 3
('301', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('302', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('303', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('304', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('305', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('306', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('307', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),
('308', 3, 'Phòng đơn Superior tầng 3', 0, NOW(), NOW()),

-- 8 Phòng đôi Superior (401-408) - room_type_id = 4
('401', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('402', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('403', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('404', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('405', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('406', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('407', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),
('408', 4, 'Phòng đôi Superior tầng 4', 0, NOW(), NOW()),

-- 8 Phòng Business (501-508) - room_type_id = 5
('501', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('502', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('503', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('504', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('505', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('506', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('507', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),
('508', 5, 'Phòng Business tầng 5, dành cho khách công tác', 0, NOW(), NOW()),

-- 8 Suite gia đình (601-608) - room_type_id = 6
('601', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('602', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('603', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('604', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('605', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('606', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('607', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW()),
('608', 6, 'Suite gia đình tầng 6, sang trọng và rộng rãi', 0, NOW(), NOW());

-- ===============================================
-- 5. ROOM TYPE AMENITIES (Tiện nghi cho từng loại phòng)
-- ===============================================

-- Phòng đơn Standard (room_type_id=1): WiFi, TV, Điều hòa, Tủ lạnh, Dép
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()),
(1, 2, NOW(), NOW()),
(1, 3, NOW(), NOW()),
(1, 4, NOW(), NOW()),
(1, 10, NOW(), NOW());

-- Phòng đôi Standard (room_type_id=2): WiFi, TV, Điều hòa, Tủ lạnh, Dép
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(2, 1, NOW(), NOW()),
(2, 2, NOW(), NOW()),
(2, 3, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(2, 10, NOW(), NOW());

-- Phòng đơn Superior (room_type_id=3): WiFi, TV, Điều hòa, Tủ lạnh, Két sắt, Máy sấy tóc, Dép
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(3, 1, NOW(), NOW()),
(3, 2, NOW(), NOW()),
(3, 3, NOW(), NOW()),
(3, 4, NOW(), NOW()),
(3, 5, NOW(), NOW()),
(3, 8, NOW(), NOW()),
(3, 10, NOW(), NOW());

-- Phòng đôi Superior (room_type_id=4): WiFi, TV, Điều hòa, Tủ lạnh, Két sắt, Máy sấy tóc, Dép
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(4, 1, NOW(), NOW()),
(4, 2, NOW(), NOW()),
(4, 3, NOW(), NOW()),
(4, 4, NOW(), NOW()),
(4, 5, NOW(), NOW()),
(4, 8, NOW(), NOW()),
(4, 10, NOW(), NOW());

-- Phòng Business (room_type_id=5): WiFi, TV, Điều hòa, Tủ lạnh, Két sắt, Bồn tắm, Máy sấy tóc, Máy pha cà phê, Dép
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(5, 1, NOW(), NOW()),
(5, 2, NOW(), NOW()),
(5, 3, NOW(), NOW()),
(5, 4, NOW(), NOW()),
(5, 5, NOW(), NOW()),
(5, 6, NOW(), NOW()),
(5, 8, NOW(), NOW()),
(5, 9, NOW(), NOW()),
(5, 10, NOW(), NOW());

-- Suite gia đình (room_type_id=6): TẤT CẢ tiện nghi
INSERT INTO room_type_amenities (room_type_id, amenity_id, created_at, updated_at) VALUES
(6, 1, NOW(), NOW()),
(6, 2, NOW(), NOW()),
(6, 3, NOW(), NOW()),
(6, 4, NOW(), NOW()),
(6, 5, NOW(), NOW()),
(6, 6, NOW(), NOW()),
(6, 7, NOW(), NOW()),
(6, 8, NOW(), NOW()),
(6, 9, NOW(), NOW()),
(6, 10, NOW(), NOW());

-- ===============================================
-- 6. ROOM AVAILABILITIES (60 ngày tới cho tất cả phòng)
-- ===============================================

-- Tạo availability cho 60 ngày tới
INSERT INTO room_availabilities (room_id, available_date, price, is_available, created_at, updated_at)
SELECT 
    r.id,
    DATE_ADD(CURDATE(), INTERVAL seq DAY) AS available_date,
    rt.price * (1 + CASE 
        WHEN DAYOFWEEK(DATE_ADD(CURDATE(), INTERVAL seq DAY)) IN (1, 7) THEN 0.3  -- Cuối tuần +30%
        ELSE 0
    END) AS price,
    1 AS is_available,
    NOW() AS created_at,
    NOW() AS updated_at
FROM rooms r
CROSS JOIN room_types rt
CROSS JOIN (
    SELECT 0 seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION 
    SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION
    SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION 
    SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION
    SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION 
    SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION
    SELECT 30 UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION 
    SELECT 35 UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION
    SELECT 40 UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION 
    SELECT 45 UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION
    SELECT 50 UNION SELECT 51 UNION SELECT 52 UNION SELECT 53 UNION SELECT 54 UNION 
    SELECT 55 UNION SELECT 56 UNION SELECT 57 UNION SELECT 58 UNION SELECT 59
) days
WHERE r.room_type_id = rt.id;

-- ===============================================
-- 7. BOOKINGS (20 bookings với các trạng thái khác nhau)
-- ===============================================

INSERT INTO bookings (user_id, booking_code, status, payment_method, transaction_id, amount, decline_reason, created_at, updated_at) VALUES
-- 5 bookings COMPLETED (đã hoàn thành)
(2, 'BK0001', 5, 'VNPAY', 'VNP20241001123456', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY)),
(3, 'BK0002', 5, 'MOMO', 'MM20241002234567', 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),
(4, 'BK0003', 5, 'VNPAY', 'VNP20241003345678', 3600000.00, NULL, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(5, 'BK0004', 5, 'BANKING', 'BANK20241004456789', 6000000.00, NULL, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(6, 'BK0005', 5, 'VNPAY', 'VNP20241005567890', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- 3 bookings PAYMENT_COMPLETED (đã thanh toán, sắp checkin)
(2, 'BK0006', 3, 'VNPAY', 'VNP20241106001122', 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'BK0007', 3, 'MOMO', 'MM20241106002233', 3600000.00, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
(4, 'BK0008', 3, 'BANKING', 'BANK20241106003344', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),

-- 3 bookings PAYMENT_PENDING (chờ thanh toán)
(5, 'BK0009', 2, NULL, NULL, 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'BK0010', 2, NULL, NULL, 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'BK0011', 2, NULL, NULL, 3600000.00, NULL, NOW(), NOW()),

-- 4 bookings CONFIRMED (đã xác nhận, chờ thanh toán)
(3, 'BK0012', 1, NULL, NULL, 6000000.00, NULL, NOW(), NOW()),
(4, 'BK0013', 1, NULL, NULL, 2400000.00, NULL, NOW(), NOW()),
(5, 'BK0014', 1, NULL, NULL, 1500000.00, NULL, NOW(), NOW()),
(6, 'BK0015', 1, NULL, NULL, 3600000.00, NULL, NOW(), NOW()),

-- 3 bookings PENDING (chờ xác nhận)
(2, 'BK0016', 0, NULL, NULL, 1500000.00, NULL, NOW(), NOW()),
(3, 'BK0017', 0, NULL, NULL, 2400000.00, NULL, NOW(), NOW()),
(4, 'BK0018', 0, NULL, NULL, 3600000.00, NULL, NOW(), NOW()),

-- 2 bookings CANCELLED/DECLINED
(5, 'BK0019', 4, NULL, NULL, 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),
(6, 'BK0020', 5, NULL, NULL, 2400000.00, 'Phòng không khả dụng trong thời gian yêu cầu', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));

-- ===============================================
-- 8. REQUESTS (1 request cho mỗi booking)
-- ===============================================

INSERT INTO requests (booking_id, room_id, check_in, check_out, number_of_guests, status, note, created_at, updated_at)
SELECT 
    b.id,
    -- Chọn phòng dựa vào booking_id
    CASE 
        WHEN b.id BETWEEN 1 AND 5 THEN b.id  -- COMPLETED: rooms 1-5
        WHEN b.id BETWEEN 6 AND 8 THEN b.id + 5  -- PAYMENT_COMPLETED: rooms 11-13
        WHEN b.id BETWEEN 9 AND 11 THEN b.id + 5  -- PAYMENT_PENDING: rooms 14-16
        WHEN b.id BETWEEN 12 AND 15 THEN b.id + 5  -- CONFIRMED: rooms 17-20
        WHEN b.id BETWEEN 16 AND 18 THEN b.id + 5  -- PENDING: rooms 21-23
        ELSE b.id + 5  -- CANCELLED/DECLINED: rooms 24-25
    END,
    -- Check-in date
    CASE 
        WHEN b.status IN (4, 5) THEN DATE_ADD(b.created_at, INTERVAL 5 DAY)  -- COMPLETED/CANCELLED: đã qua
        ELSE DATE_ADD(NOW(), INTERVAL 7 DAY)  -- Tương lai: 7 ngày sau
    END,
    -- Check-out date
    CASE 
        WHEN b.status IN (4, 5) THEN DATE_ADD(b.created_at, INTERVAL 8 DAY)  -- 3 ngày lưu trú
        ELSE DATE_ADD(NOW(), INTERVAL 10 DAY)  -- 3 ngày lưu trú
    END,
    2,  -- number_of_guests
    -- Status của request tương ứng với booking
    CASE b.status
        WHEN 0 THEN 0  -- PENDING
        WHEN 1 THEN 1  -- CONFIRMED
        WHEN 2 THEN 2  -- PAYMENT_PENDING
        WHEN 3 THEN 3  -- PAYMENT_COMPLETED
        WHEN 4 THEN 6  -- CANCELLED
        WHEN 5 THEN 7  -- COMPLETED
    END,
    CASE 
        WHEN b.status = 0 THEN 'Vui lòng xác nhận đặt phòng'
        WHEN b.status = 1 THEN 'Đã xác nhận, chờ thanh toán'
        WHEN b.status = 2 THEN 'Đang chờ thanh toán'
        WHEN b.status = 3 THEN 'Đã thanh toán, chờ check-in'
        WHEN b.status = 4 THEN 'Booking đã bị hủy'
        WHEN b.status = 5 THEN 'Đã hoàn thành'
        ELSE NULL
    END,
    b.created_at,
    b.updated_at
FROM bookings b;

-- ===============================================
-- 9. GUESTS (2 guests cho mỗi request)
-- ===============================================

INSERT INTO guests (request_id, full_name, identity_type, identity_number, identity_issued_date, identity_issued_place, created_at, updated_at)
SELECT 
    r.id,
    CONCAT('Khách ', r.id, 'A'),
    1,  -- CCCD
    CONCAT('0012345678', LPAD(r.id, 2, '0')),
    DATE_SUB(CURDATE(), INTERVAL 2 YEAR),
    'TP. Hồ Chí Minh',
    NOW(),
    NOW()
FROM requests r;

INSERT INTO guests (request_id, full_name, identity_type, identity_number, identity_issued_date, identity_issued_place, created_at, updated_at)
SELECT 
    r.id,
    CONCAT('Khách ', r.id, 'B'),
    1,  -- CCCD
    CONCAT('0098765432', LPAD(r.id, 2, '0')),
    DATE_SUB(CURDATE(), INTERVAL 1 YEAR),
    'Hà Nội',
    NOW(),
    NOW()
FROM requests r;

-- ===============================================
-- 10. REVIEWS (chỉ cho các request COMPLETED)
-- ===============================================

INSERT INTO reviews (user_id, request_id, rating, comment, created_at, updated_at)
SELECT 
    b.user_id,
    r.id,
    FLOOR(4 + RAND() * 2),  -- Rating 4-5 sao
    CASE FLOOR(RAND() * 5)
        WHEN 0 THEN 'Khách sạn tuyệt vời, phòng sạch sẽ, nhân viên thân thiện!'
        WHEN 1 THEN 'Trải nghiệm tốt, vị trí thuận lợi, sẽ quay lại lần sau.'
        WHEN 2 THEN 'Phòng rộng rãi, view đẹp, giá cả hợp lý.'
        WHEN 3 THEN 'Dịch vụ chuyên nghiệp, tiện nghi đầy đủ, rất hài lòng.'
        ELSE 'Khách sạn đẹp, sạch sẽ, phục vụ chu đáo. Highly recommended!'
    END,
    NOW(),
    NOW()
FROM requests r
INNER JOIN bookings b ON r.booking_id = b.id
WHERE r.status = 7;  -- COMPLETED

-- ===============================================
-- 11. ROOM AVAILABILITY REQUESTS
-- ===============================================

INSERT INTO room_availability_requests (room_availability_id, request_id, created_at, updated_at)
SELECT 
    ra.id,
    r.id,
    NOW(),
    NOW()
FROM requests r
INNER JOIN room_availabilities ra ON ra.room_id = r.room_id
WHERE ra.available_date BETWEEN DATE(r.check_in) AND DATE(DATE_SUB(r.check_out, INTERVAL 1 DAY));

-- ===============================================
-- HOÀN TẤT
-- ===============================================

SELECT '✅ SEED DATA COMPLETED!' AS status;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_amenities FROM amenities;
SELECT COUNT(*) AS total_room_types FROM room_types;
SELECT COUNT(*) AS total_rooms FROM rooms;
SELECT COUNT(*) AS total_room_type_amenities FROM room_type_amenities;
SELECT COUNT(*) AS total_room_availabilities FROM room_availabilities;
SELECT COUNT(*) AS total_bookings FROM bookings;
SELECT COUNT(*) AS total_requests FROM requests;
SELECT COUNT(*) AS total_guests FROM guests;
SELECT COUNT(*) AS total_reviews FROM reviews;
SELECT COUNT(*) AS total_room_availability_requests FROM room_availability_requests;
