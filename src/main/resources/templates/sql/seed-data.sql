-- ===============================================
-- HIKARI HOTEL MANAGEMENT - SEED DATA
-- Database: hikari_hotel
-- ===============================================

-- Tạo database nếu chưa có
CREATE DATABASE IF NOT EXISTS hikari_hotel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hikari_hotel;

-- Xóa dữ liệu cũ (theo thứ tự foreign key)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE room_availability_requests;
TRUNCATE TABLE reviews;
TRUNCATE TABLE guests;
TRUNCATE TABLE requests;
TRUNCATE TABLE bookings;
TRUNCATE TABLE room_availabilities;
TRUNCATE TABLE room_amenities;
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
('admin@hikari.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin Hikari', '0901234567', '1990-01-01', 1, 1, 1, NOW(), NOW()),
('user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nguyễn Văn A', '0912345678', '1995-05-15', 0, 1, 1, NOW(), NOW()),
('user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Trần Thị B', '0923456789', '1992-08-20', 0, 1, 1, NOW(), NOW()),
('user3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lê Văn C', '0934567890', '1998-03-10', 0, 1, 1, NOW(), NOW()),
('user4@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Phạm Thị D', '0945678901', '1994-11-25', 0, 1, 1, NOW(), NOW()),
('user5@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hoàng Văn E', '0956789012', '1996-07-30', 0, 1, 1, NOW(), NOW());

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
-- 3. ROOM TYPES (4 loại phòng)
-- ===============================================
INSERT INTO room_types (name, description, price, created_at, updated_at) VALUES
('Standard', 'Phòng tiêu chuẩn - 20m² - 1 giường đôi', 500000.00, NOW(), NOW()),
('Superior', 'Phòng cao cấp - 30m² - 1 giường đôi + sofa', 800000.00, NOW(), NOW()),
('Deluxe', 'Phòng deluxe - 40m² - 2 giường đôi + ban công', 1200000.00, NOW(), NOW()),
('Suite', 'Phòng suite - 60m² - 1 giường king + phòng khách riêng', 2000000.00, NOW(), NOW());

-- ===============================================
-- 4. ROOMS (40 phòng: 10 Standard, 15 Superior, 10 Deluxe, 5 Suite)
-- ===============================================

-- 10 Standard (101-110)
INSERT INTO rooms (room_number, room_type_id, capacity, description, created_at, updated_at) VALUES
('101', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('102', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('103', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('104', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('105', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('106', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('107', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('108', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('109', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),
('110', 1, 2, 'Phòng Standard tầng 1, view sân vườn', NOW(), NOW()),

-- 15 Superior (201-215)
('201', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('202', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('203', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('204', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('205', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('206', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('207', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('208', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('209', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('210', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('211', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('212', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('213', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('214', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),
('215', 2, 2, 'Phòng Superior tầng 2, view thành phố', NOW(), NOW()),

-- 10 Deluxe (301-310)
('301', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('302', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('303', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('304', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('305', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('306', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('307', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('308', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('309', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),
('310', 3, 4, 'Phòng Deluxe tầng 3, view biển, có ban công', NOW(), NOW()),

-- 5 Suite (401-405)
('401', 4, 4, 'Suite tầng 4, view biển toàn cảnh, phòng khách riêng', NOW(), NOW()),
('402', 4, 4, 'Suite tầng 4, view biển toàn cảnh, phòng khách riêng', NOW(), NOW()),
('403', 4, 4, 'Suite tầng 4, view biển toàn cảnh, phòng khách riêng', NOW(), NOW()),
('404', 4, 4, 'Suite tầng 4, view biển toàn cảnh, phòng khách riêng', NOW(), NOW()),
('405', 4, 4, 'Suite tầng 4, view biển toàn cảnh, phòng khách riêng', NOW(), NOW());

-- ===============================================
-- 5. ROOM AMENITIES (Tiện nghi cho từng phòng)
-- ===============================================

-- Standard rooms (101-110): WiFi, TV, Điều hòa, Tủ lạnh, Dép
INSERT INTO room_amenities (room_id, amenity_id, created_at, updated_at)
SELECT r.id, a.id, NOW(), NOW()
FROM rooms r
CROSS JOIN amenities a
WHERE r.room_type_id = 1 
  AND a.id IN (1, 2, 3, 4, 10);

-- Superior rooms (201-215): WiFi, TV, Điều hòa, Tủ lạnh, Két sắt, Máy sấy tóc, Dép
INSERT INTO room_amenities (room_id, amenity_id, created_at, updated_at)
SELECT r.id, a.id, NOW(), NOW()
FROM rooms r
CROSS JOIN amenities a
WHERE r.room_type_id = 2 
  AND a.id IN (1, 2, 3, 4, 5, 8, 10);

-- Deluxe rooms (301-310): WiFi, TV, Điều hòa, Tủ lạnh, Két sắt, Bồn tắm, Ban công, Máy sấy tóc, Dép
INSERT INTO room_amenities (room_id, amenity_id, created_at, updated_at)
SELECT r.id, a.id, NOW(), NOW()
FROM rooms r
CROSS JOIN amenities a
WHERE r.room_type_id = 3 
  AND a.id IN (1, 2, 3, 4, 5, 6, 7, 8, 10);

-- Suite rooms (401-405): TẤT CẢ tiện nghi
INSERT INTO room_amenities (room_id, amenity_id, created_at, updated_at)
SELECT r.id, a.id, NOW(), NOW()
FROM rooms r
CROSS JOIN amenities a
WHERE r.room_type_id = 4;

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

INSERT INTO bookings (user_id, booking_code, booking_date, status, created_at, updated_at) VALUES
-- 5 bookings COMPLETED (đã hoàn thành)
(2, 'BK0001', DATE_SUB(NOW(), INTERVAL 60 DAY), 5, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY)),
(3, 'BK0002', DATE_SUB(NOW(), INTERVAL 50 DAY), 5, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),
(4, 'BK0003', DATE_SUB(NOW(), INTERVAL 40 DAY), 5, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(5, 'BK0004', DATE_SUB(NOW(), INTERVAL 30 DAY), 5, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(6, 'BK0005', DATE_SUB(NOW(), INTERVAL 20 DAY), 5, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- 3 bookings PAYMENT_COMPLETED (đã thanh toán, sắp checkin)
(2, 'BK0006', DATE_SUB(NOW(), INTERVAL 3 DAY), 3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 'BK0007', DATE_SUB(NOW(), INTERVAL 5 DAY), 3, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
(4, 'BK0008', DATE_SUB(NOW(), INTERVAL 7 DAY), 3, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),

-- 3 bookings PAYMENT_PENDING (chờ thanh toán)
(5, 'BK0009', DATE_SUB(NOW(), INTERVAL 2 DAY), 2, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'BK0010', DATE_SUB(NOW(), INTERVAL 1 DAY), 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'BK0011', NOW(), 2, NOW(), NOW()),

-- 4 bookings CONFIRMED (đã xác nhận, chờ thanh toán)
(3, 'BK0012', NOW(), 1, NOW(), NOW()),
(4, 'BK0013', NOW(), 1, NOW(), NOW()),
(5, 'BK0014', NOW(), 1, NOW(), NOW()),
(6, 'BK0015', NOW(), 1, NOW(), NOW()),

-- 3 bookings PENDING (chờ xác nhận)
(2, 'BK0016', NOW(), 0, NOW(), NOW()),
(3, 'BK0017', NOW(), 0, NOW(), NOW()),
(4, 'BK0018', NOW(), 0, NOW(), NOW()),

-- 2 bookings CANCELLED/DECLINED
(5, 'BK0019', DATE_SUB(NOW(), INTERVAL 10 DAY), 4, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),
(6, 'BK0020', DATE_SUB(NOW(), INTERVAL 8 DAY), 5, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));

-- ===============================================
-- 8. REQUESTS (1 request cho mỗi booking)
-- ===============================================

INSERT INTO requests (booking_id, room_id, check_in, check_out, number_of_guests, status, created_at, updated_at)
SELECT 
    b.id,
    -- Chọn phòng dựa vào booking_id
    CASE 
        WHEN b.id BETWEEN 1 AND 5 THEN b.id  -- COMPLETED: rooms 1-5
        WHEN b.id BETWEEN 6 AND 8 THEN b.id + 5  -- PAYMENT_COMPLETED: rooms 6-8
        WHEN b.id BETWEEN 9 AND 11 THEN b.id + 5  -- PAYMENT_PENDING: rooms 9-11
        WHEN b.id BETWEEN 12 AND 15 THEN b.id + 5  -- CONFIRMED: rooms 12-15
        WHEN b.id BETWEEN 16 AND 18 THEN b.id + 5  -- PENDING: rooms 16-18
        ELSE b.id + 5  -- CANCELLED/DECLINED: rooms 19-20
    END,
    -- Check-in date
    CASE 
        WHEN b.status = 5 THEN DATE_ADD(b.booking_date, INTERVAL 5 DAY)  -- COMPLETED: đã checkin/checkout
        ELSE DATE_ADD(NOW(), INTERVAL 7 DAY)  -- Tương lai: 7 ngày sau
    END,
    -- Check-out date
    CASE 
        WHEN b.status = 5 THEN DATE_ADD(b.booking_date, INTERVAL 8 DAY)  -- COMPLETED: 3 ngày lưu trú
        ELSE DATE_ADD(NOW(), INTERVAL 10 DAY)  -- Tương lai: 3 ngày lưu trú
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
SELECT COUNT(*) AS total_room_amenities FROM room_amenities;
SELECT COUNT(*) AS total_room_availabilities FROM room_availabilities;
SELECT COUNT(*) AS total_bookings FROM bookings;
SELECT COUNT(*) AS total_requests FROM requests;
SELECT COUNT(*) AS total_guests FROM guests;
SELECT COUNT(*) AS total_reviews FROM reviews;
SELECT COUNT(*) AS total_room_availability_requests FROM room_availability_requests;
