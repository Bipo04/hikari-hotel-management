-- ===============================================
-- HIKARI HOTEL MANAGEMENT - SEED DATA
-- Database: HikariHotel
-- ===============================================

-- Tạo database nếu chưa có
USE `hikari_hotel`;

-- Xóa dữ liệu cũ (theo thứ tự foreign key)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE room_availability_requests;
TRUNCATE TABLE reviews;
TRUNCATE TABLE guests;
TRUNCATE TABLE requests;
TRUNCATE TABLE bookings;
TRUNCATE TABLE room_availabilities;
TRUNCATE TABLE room_type_amenities;
TRUNCATE TABLE room_type_images;
TRUNCATE TABLE rooms;
TRUNCATE TABLE room_types;
TRUNCATE TABLE amenities;
TRUNCATE TABLE users;
TRUNCATE TABLE customer_tiers;
SET FOREIGN_KEY_CHECKS = 1;

-- ===============================================
-- 0. CUSTOMER TIERS (4 hạng khách hàng)
-- ===============================================
INSERT INTO customer_tiers (code, name, min_spending, min_bookings, discount_percent, tier_order, description, active, created_at, updated_at) VALUES
('BRONZE', 'Đồng', 0, 0, 0, 1, 'Hạng mặc định cho khách hàng mới', 1, NOW(), NOW()),
('SILVER', 'Bạc', 20000000, 10, 5, 2, 'Dành cho khách hàng có từ 10 booking hoặc chi tiêu từ 20 triệu đồng', 1, NOW(), NOW()),
('GOLD', 'Vàng', 50000000, 20, 10, 3, 'Dành cho khách hàng có từ 20 booking hoặc chi tiêu từ 50 triệu đồng', 1, NOW(), NOW()),
('DIAMOND', 'Kim Cương', 100000000, 40, 15, 4, 'Hạng cao nhất dành cho khách hàng VIP', 1, NOW(), NOW());

-- ===============================================
-- 1. USERS (6 users: 1 admin + 5 regular users)
-- Password: password123
-- role: 0=USER, 1=ADMIN (EnumType.ORDINAL)
-- customer_tier_id: 1=BRONZE (mặc định), 2=SILVER, 3=GOLD, 4=DIAMOND
-- ===============================================
INSERT INTO users (email, password, name, phone, birth_date, role, status, is_verified, customer_tier_id, total_spent, total_bookings, created_at, updated_at) VALUES
('admin@hikari.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Admin Hikari', '0901234567', '1990-01-01', 1, 1, 1, 1, 0, 0, NOW(), NOW()),
('user1@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Nguyễn Văn A', '0912345678', '1995-05-15', 0, 1, 1, 1, 0, 0, NOW(), NOW()),
('user2@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Trần Thị B', '0923456789', '1992-08-20', 0, 1, 1, 2, 25000000, 12, NOW(), NOW()),
('user3@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Lê Văn C', '0934567890', '1998-03-10', 0, 1, 1, 3, 55000000, 22, NOW(), NOW()),
('user4@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Phạm Thị D', '0945678901', '1994-11-25', 0, 1, 1, 4, 120000000, 45, NOW(), NOW()),
('user5@example.com', '$2a$10$wxKEMAva8GNE.iZwM3k9iOFJtSVjDos9nI/EtmDTk8qxePVFkKwPO', 'Hoàng Văn E', '0956789012', '1996-07-30', 0, 1, 1, 1, 5000000, 3, NOW(), NOW());

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
-- class: 0=STANDARD, 1=SUPERIOR, 2=BUSINESS, 3=SUITE
-- ===============================================
INSERT INTO room_types (name, class, description, capacity, price, created_at, updated_at) VALUES
('Phòng đơn Standard', 0, 'Phòng cơ bản cho 1 người với đầy đủ tiện nghi thiết yếu', 1, 500000.00, NOW(), NOW()),
('Phòng đôi Standard', 0, 'Phòng cơ bản cho 2 người với 2 giường đơn hoặc 1 giường đôi', 2, 800000.00, NOW(), NOW()),
('Phòng đơn Superior', 1, 'Phòng cao cấp hơn với không gian rộng rãi và view đẹp', 1, 700000.00, NOW(), NOW()),
('Phòng đôi Superior', 1, 'Phòng cao cấp cho 2 người với nhiều tiện nghi hơn', 2, 1000000.00, NOW(), NOW()),
('Phòng Business', 2, 'Phòng dành cho khách công tác với bàn làm việc riêng', 2, 1200000.00, NOW(), NOW()),
('Suite gia đình', 3, 'Phòng sang trọng rộng rãi cho gia đình với phòng khách riêng', 4, 2500000.00, NOW(), NOW());

-- ===============================================
-- 3.1 ROOM TYPE IMAGES (Ảnh cho từng loại phòng)
-- ===============================================
INSERT INTO room_type_images (room_type_id, image_url, image_key, is_primary, created_at, updated_at) VALUES
-- Phòng đơn Standard
(1, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(1, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW()),

-- Phòng đôi Standard
(2, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(2, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW()),

-- Phòng đơn Superior
(3, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(3, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW()),

-- Phòng đôi Superior
(4, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(4, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW()),

-- Phòng Business
(5, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(5, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW()),

-- Suite gia đình
(6, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 1, NOW(), NOW()),
(6, 'https://pub-058f4a2577bd4b8780cec0486129191d.r2.dev/deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 'deb2a2e8-9426-4e8b-85f9-aa9056cbeaf3.jpeg', 0, NOW(), NOW());

-- ===============================================
-- 4. ROOMS (48 phòng: 8 phòng đơn Standard, 8 phòng đôi Standard, 8 phòng đơn Superior, 8 phòng đôi Superior, 8 phòng Business, 8 Suite)
-- status: 0=AVAILABLE, 1=MAINTENANCE, 2=CLEANING, 3=OUT_OF_SERVICE
-- ===============================================

-- 8 Phòng đơn Standard (101-108) - room_type_id = 1
INSERT INTO rooms (room_number, room_type_id, description, status, created_at, updated_at) VALUES
('101', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('102', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('103', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('104', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('105', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('106', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),
('107', 1, 'Phòng đơn Standard tầng 1', 2, NOW(), NOW()),  -- CLEANING
('108', 1, 'Phòng đơn Standard tầng 1', 0, NOW(), NOW()),

-- 8 Phòng đôi Standard (201-208) - room_type_id = 2
('201', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('202', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('203', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('204', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('205', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('206', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('207', 2, 'Phòng đôi Standard tầng 2', 0, NOW(), NOW()),
('208', 2, 'Phòng đôi Standard tầng 2', 1, NOW(), NOW()),  -- MAINTENANCE

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
-- 7. BOOKINGS (15 bookings với các trạng thái khác nhau)
-- status: 0=PAYMENT_PENDING, 1=PAYMENT_COMPLETED, 2=CANCELLED, 3=DECLINED
-- ===============================================

INSERT INTO bookings (user_id, booking_code, status, payment_method, amount, decline_reason, created_at, updated_at) VALUES
-- 5 bookings PAYMENT_COMPLETED (đã thanh toán, đã hoàn thành)
(2, 'BK0001', 1, 'VNPAY', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY)),
(3, 'BK0002', 1, 'VNPAY', 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY)),
(4, 'BK0003', 1, 'VNPAY', 3600000.00, NULL, DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY)),
(5, 'BK0004', 1, 'VNPAY', 6000000.00, NULL, DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
(6, 'BK0005', 1, 'VNPAY', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),

-- 3 bookings PAYMENT_COMPLETED (đã thanh toán, sắp checkin)
(2, 'BK0006', 1, 'VNPAY', 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 'BK0007', 1, 'VNPAY', 3600000.00, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, 'BK0008', 1, 'VNPAY', 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 3 bookings PAYMENT_PENDING (chờ thanh toán)
(5, 'BK0009', 0, NULL, 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'BK0010', 0, NULL, 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'BK0011', 0, NULL, 3600000.00, NULL, NOW(), NOW()),

-- 2 bookings CANCELLED
(3, 'BK0012', 2, NULL, 1500000.00, NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),
(4, 'BK0013', 2, NULL, 2400000.00, NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 2 bookings DECLINED
(5, 'BK0014', 3, NULL, 3600000.00, 'Phòng không khả dụng trong thời gian yêu cầu', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(6, 'BK0015', 3, NULL, 6000000.00, 'Thông tin đặt phòng không hợp lệ', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- ===============================================
-- 8. REQUESTS (1-2 requests cho mỗi booking)
-- status: 0=PAYMENT_PENDING, 1=PAYMENT_COMPLETED, 2=CHECKED_IN, 3=CHECKED_OUT, 4=CANCELLED
-- ===============================================

INSERT INTO requests (booking_id, room_id, check_in, check_out, number_of_guests, status, note, created_at, updated_at) VALUES
-- Requests cho bookings PAYMENT_COMPLETED đã qua (1-5): status = CHECKED_OUT (3)
(1, 1, DATE_SUB(NOW(), INTERVAL 58 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), 1, 3, 'Đã check-out', DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY)),
(2, 9, DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), 2, 3, 'Đã check-out', DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),
(3, 17, DATE_SUB(NOW(), INTERVAL 38 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY), 1, 3, 'Đã check-out', DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 35 DAY)),
(4, 25, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), 2, 3, 'Đã check-out', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY)),
(5, 2, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), 1, 3, 'Đã check-out', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- Requests cho bookings PAYMENT_COMPLETED sắp tới (6-8): status = PAYMENT_COMPLETED (1)
(6, 10, DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY), 2, 1, 'Đã thanh toán, chờ check-in', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(7, 18, DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY), 2, 1, 'Đã thanh toán, chờ check-in', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(8, 3, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 4 DAY), 1, 1, 'Đã thanh toán, chờ check-in', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- Requests cho bookings PAYMENT_PENDING (9-11): status = PAYMENT_PENDING (0)
(9, 11, DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 8 DAY), 2, 0, 'Chờ thanh toán', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(10, 4, DATE_ADD(NOW(), INTERVAL 4 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 0, 'Chờ thanh toán', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(11, 19, DATE_ADD(NOW(), INTERVAL 6 DAY), DATE_ADD(NOW(), INTERVAL 9 DAY), 2, 0, 'Chờ thanh toán', NOW(), NOW()),

-- Requests cho bookings CANCELLED (12-13): status = CANCELLED (4)
(12, 5, DATE_ADD(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 13 DAY), 1, 4, 'Khách hủy', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY)),
(13, 12, DATE_ADD(NOW(), INTERVAL 8 DAY), DATE_ADD(NOW(), INTERVAL 11 DAY), 2, 4, 'Khách hủy', DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- Requests cho bookings DECLINED (14-15): status = CANCELLED (4)
(14, 20, DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 2, 4, 'Admin từ chối', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(15, 26, DATE_ADD(NOW(), INTERVAL 9 DAY), DATE_ADD(NOW(), INTERVAL 12 DAY), 4, 4, 'Admin từ chối', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- ===============================================
-- 9. GUESTS (2 guests cho mỗi request, trừ request có 1 khách)
-- identity_type: 0=CMND, 1=CCCD, 2=PASSPORT, 3=DRIVER_LICENSE
-- ===============================================

INSERT INTO guests (request_id, full_name, identity_type, identity_number, identity_issued_date, identity_issued_place, created_at, updated_at) VALUES
-- Request 1 (1 guest)
(1, 'Nguyễn Văn An', 1, '001234567801', '2020-01-15', 'TP. Hồ Chí Minh', NOW(), NOW()),

-- Request 2 (2 guests)
(2, 'Trần Thị Bích', 1, '001234567802', '2020-02-20', 'Hà Nội', NOW(), NOW()),
(2, 'Trần Văn Cường', 1, '001234567803', '2019-05-10', 'Hà Nội', NOW(), NOW()),

-- Request 3 (1 guest)
(3, 'Lê Thị Diễm', 1, '001234567804', '2021-03-12', 'Đà Nẵng', NOW(), NOW()),

-- Request 4 (2 guests)
(4, 'Phạm Văn Đức', 1, '001234567805', '2020-07-25', 'Cần Thơ', NOW(), NOW()),
(4, 'Phạm Thị Hoa', 1, '001234567806', '2021-08-30', 'Cần Thơ', NOW(), NOW()),

-- Request 5 (1 guest)
(5, 'Hoàng Văn Hùng', 1, '001234567807', '2019-11-05', 'Huế', NOW(), NOW()),

-- Request 6 (2 guests)
(6, 'Võ Thị Lan', 1, '001234567808', '2020-04-18', 'TP. Hồ Chí Minh', NOW(), NOW()),
(6, 'Võ Văn Long', 1, '001234567809', '2021-06-22', 'TP. Hồ Chí Minh', NOW(), NOW()),

-- Request 7 (2 guests)
(7, 'Đỗ Thị Mai', 1, '001234567810', '2020-09-14', 'Hà Nội', NOW(), NOW()),
(7, 'Đỗ Văn Nam', 1, '001234567811', '2019-12-20', 'Hà Nội', NOW(), NOW()),

-- Request 8 (1 guest)
(8, 'Bùi Văn Phong', 1, '001234567812', '2021-01-08', 'Nha Trang', NOW(), NOW()),

-- Request 9 (2 guests)
(9, 'Ngô Thị Quỳnh', 1, '001234567813', '2020-10-16', 'Vũng Tàu', NOW(), NOW()),
(9, 'Ngô Văn Sơn', 1, '001234567814', '2021-02-28', 'Vũng Tàu', NOW(), NOW()),

-- Request 10 (1 guest)
(10, 'Lý Thị Tâm', 1, '001234567815', '2019-07-11', 'Đà Lạt', NOW(), NOW()),

-- Request 11 (2 guests)
(11, 'Trương Văn Tùng', 1, '001234567816', '2020-05-19', 'Quy Nhơn', NOW(), NOW()),
(11, 'Trương Thị Uyên', 1, '001234567817', '2021-09-23', 'Quy Nhơn', NOW(), NOW()),

-- Request 12 (1 guest) - CANCELLED
(12, 'Phan Văn Vinh', 1, '001234567818', '2020-11-07', 'Phan Thiết', NOW(), NOW()),

-- Request 13 (2 guests) - CANCELLED
(13, 'Dương Thị Xuân', 1, '001234567819', '2019-08-15', 'TP. Hồ Chí Minh', NOW(), NOW()),
(13, 'Dương Văn Yên', 1, '001234567820', '2021-04-29', 'TP. Hồ Chí Minh', NOW(), NOW()),

-- Request 14 (2 guests) - DECLINED
(14, 'Hồ Thị Ánh', 2, 'P1234567', '2020-06-10', 'Vietnam', NOW(), NOW()),
(14, 'Hồ Văn Bình', 2, 'P1234568', '2021-07-14', 'Vietnam', NOW(), NOW()),

-- Request 15 (4 guests) - DECLINED
(15, 'Mai Văn Cường', 1, '001234567821', '2019-09-21', 'Hà Nội', NOW(), NOW()),
(15, 'Mai Thị Dung', 1, '001234567822', '2020-12-03', 'Hà Nội', NOW(), NOW()),
(15, 'Mai Văn Em', 1, '001234567823', '2018-03-17', 'Hà Nội', NOW(), NOW()),
(15, 'Mai Thị Giang', 1, '001234567824', '2017-05-25', 'Hà Nội', NOW(), NOW());

-- ===============================================
-- 10. REVIEWS (chỉ cho các requests đã CHECKED_OUT)
-- ===============================================

INSERT INTO reviews (user_id, booking_id, rating, comment, created_at, updated_at) VALUES
-- Reviews cho 5 bookings đã hoàn thành (booking 1-5)
(2, 1, 5, 'Khách sạn tuyệt vời! Phòng sạch sẽ, nhân viên thân thiện và chuyên nghiệp. Tôi sẽ quay lại lần sau.', DATE_SUB(NOW(), INTERVAL 54 DAY), DATE_SUB(NOW(), INTERVAL 54 DAY)),
(3, 2, 4, 'Trải nghiệm tốt, vị trí thuận lợi. Giá cả hợp lý so với chất lượng phòng.', DATE_SUB(NOW(), INTERVAL 44 DAY), DATE_SUB(NOW(), INTERVAL 44 DAY)),
(4, 3, 5, 'Phòng rộng rãi, view đẹp, tiện nghi đầy đủ. Rất hài lòng với dịch vụ!', DATE_SUB(NOW(), INTERVAL 34 DAY), DATE_SUB(NOW(), INTERVAL 34 DAY)),
(5, 4, 5, 'Dịch vụ 5 sao! Phòng Suite thật sự sang trọng và thoải mái. Highly recommended!', DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY)),
(6, 5, 4, 'Phòng đẹp, sạch sẽ, phục vụ chu đáo. Chỉ tiếc là thời gian lưu trú hơi ngắn.', DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY));

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
SELECT COUNT(*) AS total_customer_tiers FROM customer_tiers;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_amenities FROM amenities;
SELECT COUNT(*) AS total_room_types FROM room_types;
SELECT COUNT(*) AS total_room_type_images FROM room_type_images;
SELECT COUNT(*) AS total_rooms FROM rooms;
SELECT COUNT(*) AS total_room_type_amenities FROM room_type_amenities;
SELECT COUNT(*) AS total_room_availabilities FROM room_availabilities;
SELECT COUNT(*) AS total_bookings FROM bookings;
SELECT COUNT(*) AS total_requests FROM requests;
SELECT COUNT(*) AS total_guests FROM guests;
SELECT COUNT(*) AS total_reviews FROM reviews;
SELECT COUNT(*) AS total_room_availability_requests FROM room_availability_requests;

-- Hiển thị thông tin hạng khách hàng
SELECT 
    u.email,
    u.name,
    ct.name AS tier,
    u.total_spent,
    u.total_bookings,
    ct.discount_percent AS discount
FROM users u
LEFT JOIN customer_tiers ct ON u.customer_tier_id = ct.id
ORDER BY u.id;
