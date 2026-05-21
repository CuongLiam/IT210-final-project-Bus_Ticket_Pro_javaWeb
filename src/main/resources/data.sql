INSERT IGNORE INTO locations(id, name) VALUES
(1, 'Hà Nội'),
(2, 'Hải Phòng'),
(3, 'Nam Định'),
(4, 'Đà Nẵng'),
(5, 'Nghệ An'),
(6, 'Thanh Hóa'),
(7, 'Huế'),
(8, 'Quảng Ninh');

INSERT IGNORE INTO routes(id, from_location_id, to_location_id, distance_km) VALUES
(1, 1, 2, 120),
(2, 1, 3, 90),
(3, 1, 4, 780),
(4, 1, 5, 300),
(5, 1, 6, 160),
(6, 1, 8, 160);


INSERT IGNORE INTO users(id, username, password, role, enabled) VALUES
(1, 'admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiRoQU1YbxsB3ENLUXD0QdP7p/3W36S', 'ADMIN', true),
(2, 'staff', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiRoQU1YbxsB3ENLUXD0QdP7p/3W36S', 'STAFF', true);

INSERT IGNORE INTO user_profiles(id, full_name, phone, email, address, user_id) VALUES
(1, 'System Admin', '0900000001', 'admin@gmail.com', 'Hà Nội', 1),
(2, 'System Staff', '0900000002', 'staff@gmail.com', 'Hà Nội', 2);