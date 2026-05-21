# Bus Ticket Pro

## 1. Tổng quan dự án

**Bus Ticket Pro** là hệ thống đặt vé xe khách liên tỉnh được xây dựng bằng **Java Spring Boot**, **Gradle**, **Spring MVC**, **Spring Data JPA**, **Spring Security**, **Thymeleaf** và **MySQL**.

Hệ thống phục vụ 3 nhóm người dùng chính:

- **Passenger**: hành khách tìm chuyến, xem ghế, đặt vé, tra cứu vé, hủy vé, quản lý hồ sơ.
- **Staff**: nhân viên xem vé chờ thanh toán, xác nhận thanh toán, hủy vé chờ thanh toán.
- **Admin**: quản trị viên quản lý xe, chuyến xe và hồ sơ cá nhân.

Dự án được thiết kế theo mô hình **Monolithic Architecture**, toàn bộ giao diện, xử lý nghiệp vụ và truy cập cơ sở dữ liệu nằm trong cùng một ứng dụng Spring Boot.

---

## 2. Công nghệ sử dụng

- Java 17
- Spring Boot 3.3.5
- Gradle
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- Thymeleaf
- Bean Validation
- Lombok
- MySQL
- BCrypt Password Encoder

---

## 3. Kiến trúc tổng thể

Dự án tổ chức theo mô hình 3 lớp:

```text
Controller  ->  Service  ->  Repository  ->  Database
```

### Controller Layer

Controller chịu trách nhiệm nhận request từ trình duyệt, gọi service xử lý và trả về view Thymeleaf.

Các controller chính:

```text
controller
├── auth
│   └── AuthController
├── admin
│   ├── BusController
│   └── AdminTripController
├── passenger
│   ├── PassengerTripController
│   ├── BookingController
│   └── PassengerTicketController
├── staff
│   └── StaffTicketController
└── ProfileController
```

### Service Layer

Service chứa logic nghiệp vụ chính của hệ thống.

```text
service
├── auth
├── bus
├── trip
├── passenger
├── booking
├── ticket
├── payment
└── profile
```

### Repository Layer

Repository tương tác với database thông qua Spring Data JPA.

```text
repository
├── UserRepository
├── UserProfileRepository
├── LocationRepository
├── RouteRepository
├── BusRepository
├── TripRepository
├── SeatRepository
└── TicketRepository
```

---

## 4. Cấu trúc database

Các bảng chính:

| Bảng | Vai trò |
|---|---|
| users | Lưu tài khoản đăng nhập, mật khẩu đã hash, role |
| user_profiles | Lưu thông tin cá nhân của user |
| locations | Danh sách tỉnh/thành phố |
| routes | Tuyến đường giữa các tỉnh/thành |
| buses | Danh mục xe |
| trips | Chuyến xe cụ thể |
| seats | Ghế của từng chuyến xe |
| tickets | Vé đặt của hành khách |

### Quan hệ chính

```text
User 1 - 1 UserProfile
Location 1 - N Route
Route 1 - N Trip
Bus 1 - N Trip
Trip 1 - N Seat
Trip 1 - N Ticket
Seat 1 - N Ticket
```

Ghế được thiết kế theo **Trip**, không theo Bus.

Lý do: cùng một xe có thể chạy nhiều chuyến khác nhau. Ghế S01 của chuyến hôm nay có thể đã đặt, nhưng ghế S01 của chuyến ngày mai vẫn còn trống. Vì vậy, bảng `seats` luôn gắn với `trip_id`.

---

## 5. Enum chính

```java
Role {
    PASSENGER,
    STAFF,
    ADMIN
}
```

```java
SeatStatus {
    AVAILABLE,
    PENDING,
    BOOKED
}
```

```java
TicketStatus {
    PENDING,
    PAID,
    CANCELLED
}
```

```java
BusType {
    SEAT_29,
    SEAT_45,
    SLEEPER_34,
    SLEEPER_40
}
```

---

## 6. Luồng bảo mật và phân quyền

Dự án dùng **Spring Security**.

### Đăng ký

Hành khách đăng ký tài khoản tại:

```text
GET /register
POST /register
```

Khi đăng ký:

1. Người dùng nhập username, password, họ tên, số điện thoại, email, địa chỉ.
2. Hệ thống kiểm tra username, phone, email có bị trùng không.
3. Password được hash bằng BCrypt.
4. Tạo bản ghi trong bảng `users`.
5. Tạo bản ghi tương ứng trong bảng `user_profiles`.
6. Role mặc định là `PASSENGER`.

### Đăng nhập

Người dùng đăng nhập tại:

```text
GET /login
POST /login
```

Sau khi đăng nhập thành công, hệ thống redirect theo role:

| Role | Redirect |
|---|---|
| ADMIN | `/admin/dashboard` |
| STAFF | `/staff/dashboard` |
| PASSENGER | `/passenger/home` |

### Phân quyền URL

| URL | Quyền truy cập |
|---|---|
| `/admin/**` | ADMIN |
| `/staff/**` | STAFF, ADMIN |
| `/passenger/**` | PASSENGER |
| `/profile/**` | User đã đăng nhập |
| `/login`, `/register`, `/` | Public |

---

## 7. Seed data

Dữ liệu `locations` và `routes` được seed bằng `data.sql`.

Ví dụ:

```sql
INSERT IGNORE INTO locations(id, name) VALUES
(1, 'Hà Nội'),
(2, 'Hải Phòng'),
(3, 'Nam Định'),
(4, 'Đà Nẵng');
```

Tài khoản `admin` và `staff` được seed bằng Java `CommandLineRunner` để dùng trực tiếp `PasswordEncoder`.

Tài khoản mặc định:

```text
admin / 123456
staff / 123456
```

Lý do không seed password bằng SQL: BCrypt hash thủ công dễ sai và khó bảo trì. Seed bằng Java đảm bảo password luôn được hash đúng theo cấu hình hiện tại.

---

## 8. Luồng Admin quản lý xe

### URL chính

```text
GET  /admin/buses
GET  /admin/buses/create
POST /admin/buses/create
GET  /admin/buses/edit/{id}
POST /admin/buses/edit/{id}
POST /admin/buses/delete/{id}
```

### Flow hoạt động

1. Admin vào trang quản lý xe.
2. Hệ thống lấy danh sách xe đang active.
3. Admin có thể thêm xe mới.
4. Khi thêm xe, hệ thống validate:
   - Biển số không trống.
   - Biển số không được trùng.
   - Loại xe không trống.
   - Tổng số ghế phải lớn hơn 0.
   - Nhà xe và tài xế không trống.
5. Admin có thể sửa thông tin xe.
6. Khi xóa xe, hệ thống không xóa cứng mà dùng soft delete:
   - `active = false`

Lý do dùng soft delete: xe có thể đã được liên kết với chuyến xe. Xóa cứng dễ gây lỗi khóa ngoại và làm hỏng lịch sử dữ liệu.

---

## 9. Luồng Admin quản lý chuyến xe

### URL chính

```text
GET  /admin/trips
GET  /admin/trips/create
POST /admin/trips/create
GET  /admin/trips/edit/{id}
POST /admin/trips/edit/{id}
POST /admin/trips/delete/{id}
```

### Flow tạo chuyến

1. Admin chọn tuyến đường đã seed sẵn.
2. Admin chọn xe đang active.
3. Admin nhập thời gian khởi hành.
4. Admin nhập giá vé.
5. Hệ thống tạo bản ghi `trip`.
6. Sau khi tạo trip thành công, hệ thống tự sinh danh sách ghế trong bảng `seats`.

Ví dụ xe có 45 ghế thì tạo:

```text
S01, S02, S03, ..., S45
```

Tất cả ghế ban đầu có trạng thái:

```text
AVAILABLE
```

### Lưu ý khi sửa chuyến

Khi sửa trip, hệ thống không tự sinh lại ghế.

Lý do: nếu chuyến đã có vé, việc xóa và sinh lại ghế sẽ làm sai dữ liệu booking. Việc đổi xe sau khi có vé cần logic riêng, không nằm trong core flow hiện tại.

---

## 10. Luồng Passenger tìm chuyến và xem sơ đồ ghế

### URL chính

```text
GET  /passenger/trips/search
POST /passenger/trips/search
GET  /passenger/trips/{tripId}/seats
```

### Flow tìm chuyến

1. Passenger chọn:
   - Điểm đi
   - Điểm đến
   - Ngày đi
2. Hệ thống tìm trip theo:
   - `fromLocationId`
   - `toLocationId`
   - khoảng thời gian từ đầu ngày đến cuối ngày
   - `active = true`
3. Kết quả hiển thị danh sách chuyến phù hợp.
4. Passenger bấm xem ghế.

### Flow xem sơ đồ ghế

1. Hệ thống lấy toàn bộ ghế của trip.
2. Ghế được sort theo `seatNumber`.
3. Giao diện hiển thị sơ đồ ghế dạng lưới.
4. Màu ghế:
   - `AVAILABLE`: ghế trống, có thể bấm.
   - `PENDING`: ghế đang chờ thanh toán, bị disable.
   - `BOOKED`: ghế đã thanh toán, bị disable.

### Ý nghĩa trạng thái ghế

| SeatStatus | Ý nghĩa |
|---|---|
| AVAILABLE | Ghế còn trống |
| PENDING | Ghế đã được giữ bởi vé đang chờ thanh toán |
| BOOKED | Ghế đã thanh toán thành công |

---

## 11. Luồng đặt vé

### URL chính

```text
GET  /passenger/bookings/create?tripId={tripId}&seatId={seatId}
POST /passenger/bookings/create
GET  /passenger/bookings/success?ticketCode={ticketCode}
```

### Flow đặt vé

1. Passenger bấm vào một ghế `AVAILABLE`.
2. Hệ thống mở form đặt vé.
3. Passenger nhập:
   - Họ tên khách
   - Số điện thoại
   - Email
4. Khi submit, hệ thống xử lý trong transaction.

### Transaction đặt vé

Trong `BookingService`:

1. Tìm trip.
2. Lock seat bằng `PESSIMISTIC_WRITE`.
3. Kiểm tra seat có thuộc trip không.
4. Kiểm tra seat có trạng thái `AVAILABLE` không.
5. Tạo ticket mới:
   - `ticketCode`
   - thông tin khách
   - trip
   - seat
   - totalPrice
   - status = `PENDING`
6. Cập nhật seat:
   - `AVAILABLE -> PENDING`
7. Commit transaction.

Nếu có lỗi ở bất kỳ bước nào, toàn bộ transaction rollback.

### Chống đặt trùng ghế

Repository dùng lock:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Seat s WHERE s.id = :seatId")
Optional<Seat> findByIdForUpdate(Long seatId);
```

Điều này tương đương:

```sql
SELECT * FROM seats WHERE id = ? FOR UPDATE;
```

Nếu hai người cùng đặt một ghế, người submit sau sẽ thấy ghế không còn `AVAILABLE` và bị từ chối.

---

## 12. Luồng tra cứu vé

### URL chính

```text
GET  /passenger/tickets/lookup
POST /passenger/tickets/lookup
```

### Flow hoạt động

1. Passenger nhập:
   - Mã vé
   - Số điện thoại
2. Hệ thống tìm ticket theo `ticketCode` và `phone`.
3. Nếu tìm thấy, hệ thống trả về chi tiết vé.
4. Nếu không tìm thấy, hệ thống báo lỗi.

### Dữ liệu hiển thị

Chi tiết vé lấy bằng query JOIN nhiều bảng:

```text
tickets
seats
trips
routes
locations
buses
```

Thông tin hiển thị:

- Mã vé
- Họ tên khách
- Số điện thoại
- Email
- Tuyến đường
- Giờ khởi hành
- Biển số xe
- Loại xe
- Tên tài xế
- Ghế
- Giá vé
- Trạng thái vé

---

## 13. Luồng Staff duyệt vé

### URL chính

```text
GET  /staff/tickets/pending
POST /staff/tickets/confirm/{ticketId}
POST /staff/tickets/cancel/{ticketId}
```

### Xem vé chờ thanh toán

Staff vào:

```text
/staff/tickets/pending
```

Hệ thống hiển thị danh sách ticket có trạng thái:

```text
PENDING
```

Danh sách được join sẵn với:

- Trip
- Route
- Location
- Bus
- Seat

### Xác nhận thanh toán

Khi Staff bấm xác nhận:

1. Hệ thống lock ticket bằng `PESSIMISTIC_WRITE`.
2. Kiểm tra ticket còn `PENDING` không.
3. Kiểm tra seat đang `PENDING` không.
4. Cập nhật:
   - `ticket.status = PAID`
   - `seat.status = BOOKED`
5. Commit transaction.

### Hủy vé chờ thanh toán

Khi Staff bấm hủy:

1. Hệ thống lock ticket.
2. Kiểm tra ticket còn `PENDING` không.
3. Cập nhật:
   - `ticket.status = CANCELLED`
   - `seat.status = AVAILABLE`
4. Ghế được mở lại cho người khác đặt.

---

## 14. Luồng Passenger hủy vé chủ động

### URL chính

```text
POST /passenger/tickets/cancel
```

### Điều kiện hủy vé

Passenger chỉ được hủy vé nếu:

```text
Thời gian hiện tại cách giờ khởi hành ít nhất 12 tiếng
```

Ngoài ra:

- Vé đã `CANCELLED` không được hủy lại.
- Nếu còn dưới 12 tiếng trước giờ khởi hành, hệ thống từ chối hủy.

### Flow hủy vé

1. Passenger tra cứu vé bằng mã vé và số điện thoại.
2. Tại trang chi tiết vé, Passenger bấm hủy vé.
3. Hệ thống lock ticket theo `ticketCode + phone`.
4. Kiểm tra trạng thái vé.
5. Kiểm tra thời gian so với giờ khởi hành.
6. Nếu hợp lệ:
   - `ticket.status = CANCELLED`
   - `seat.status = AVAILABLE`
7. Ghế xuất hiện lại trên sơ đồ ghế.

---

## 15. Luồng Profile

### URL chính

```text
GET  /profile
GET  /profile/edit
POST /profile/edit
```

### Flow hoạt động

1. User đăng nhập.
2. Truy cập `/profile`.
3. Hệ thống lấy username từ `Authentication`.
4. Tìm `UserProfile` tương ứng.
5. Hiển thị thông tin cá nhân:
   - Username
   - Role
   - Họ tên
   - Số điện thoại
   - Email
   - Địa chỉ
6. User có thể cập nhật profile.
7. Khi cập nhật, hệ thống kiểm tra:
   - Phone không trùng user khác.
   - Email không trùng user khác.
   - Dữ liệu hợp lệ theo validation.

Profile dùng chung cho cả:

```text
ADMIN
STAFF
PASSENGER
```

---

## 16. Các transaction quan trọng

### Đặt vé

```text
Ticket PENDING + Seat PENDING
```

Hai thao tác này phải nằm trong cùng một transaction.

Nếu tạo ticket thành công nhưng update seat lỗi, hệ thống rollback để tránh vé mồ côi.

### Staff xác nhận thanh toán

```text
Ticket PENDING -> PAID
Seat PENDING -> BOOKED
```

Hai thao tác phải commit cùng nhau.

### Staff hủy vé

```text
Ticket PENDING -> CANCELLED
Seat PENDING -> AVAILABLE
```

### Passenger hủy vé

```text
Ticket PENDING/PAID -> CANCELLED
Seat -> AVAILABLE
```

---

## 17. Chiến lược lock dữ liệu

Hệ thống dùng `PESSIMISTIC_WRITE` cho các nghiệp vụ nhạy cảm:

- Đặt vé
- Staff xác nhận thanh toán
- Staff hủy vé
- Passenger hủy vé

Mục tiêu là tránh race condition khi nhiều request xử lý cùng một ghế hoặc cùng một vé.

---

## 18. Luồng trạng thái vé và ghế

### Khi đặt vé

```text
Seat: AVAILABLE -> PENDING
Ticket: PENDING
```

### Khi Staff xác nhận thanh toán

```text
Seat: PENDING -> BOOKED
Ticket: PENDING -> PAID
```

### Khi Staff hủy vé chờ thanh toán

```text
Seat: PENDING -> AVAILABLE
Ticket: PENDING -> CANCELLED
```

### Khi Passenger hủy vé hợp lệ

```text
Seat: PENDING/BOOKED -> AVAILABLE
Ticket: PENDING/PAID -> CANCELLED
```

---

## 19. Kiểm thử nhanh

### Tài khoản test

```text
admin / 123456
staff / 123456
```

Passenger có thể tự đăng ký tại:

```text
/register
```

### Flow demo đề xuất

1. Login admin.
2. Tạo xe tại `/admin/buses`.
3. Tạo chuyến tại `/admin/trips`.
4. Login passenger.
5. Tìm chuyến tại `/passenger/trips/search`.
6. Xem sơ đồ ghế.
7. Chọn ghế và đặt vé.
8. Tra cứu vé tại `/passenger/tickets/lookup`.
9. Login staff.
10. Xem vé pending tại `/staff/tickets/pending`.
11. Xác nhận thanh toán.
12. Quay lại passenger xem ghế đã chuyển sang booked.
13. Tạo vé khác.
14. Hủy vé và kiểm tra ghế được giải phóng.
15. Vào `/profile` để test quản lý hồ sơ.

---

## 20. Các điểm đã đáp ứng trong CORE

| Core | Nội dung | Trạng thái |
|---|---|---|
| CORE-01 | Đăng ký, đăng nhập, BCrypt | Hoàn thành |
| CORE-02 | Phân quyền Passenger / Staff / Admin | Hoàn thành |
| CORE-03 | Quản lý hồ sơ cá nhân | Hoàn thành |
| CORE-04 | Admin CRUD Bus, Location/Route seed sẵn | Hoàn thành |
| CORE-05 | Tìm chuyến, xem sơ đồ ghế, disable ghế đã đặt | Hoàn thành |
| CORE-06 | Đặt vé transaction, lock ghế, rollback khi lỗi | Hoàn thành |
| CORE-07 | Tra cứu vé bằng JOIN nhiều bảng | Hoàn thành |
| CORE-08 | Staff xác nhận thanh toán, hủy vé pending | Hoàn thành |
| CORE-09 | Passenger hủy vé trước 12 tiếng | Hoàn thành |

---

## 21. Hướng mở rộng đề xuất

### Tự động hủy vé quá hạn

Dùng `@Scheduled` để mỗi 10 phút quét:

```text
Ticket PENDING quá 30 phút
```

Sau đó tự động:

```text
Ticket -> CANCELLED
Seat -> AVAILABLE
```

### Gửi email xác nhận

Sau khi đặt vé thành công, hệ thống gửi email chứa:

- Mã vé
- Thông tin chuyến
- Ghế
- Trạng thái thanh toán

Có thể dùng `JavaMailSender` và `@Async`.

### Dashboard thống kê

Admin có thể xem:

- Doanh thu theo tháng
- Doanh thu theo tuyến
- Top chuyến được đặt nhiều nhất
- Số vé pending / paid / cancelled

Nên dùng JPQL hoặc native query `GROUP BY`, không tính toán bằng vòng lặp Java.

---

## 22. Ghi chú kỹ thuật

- Không expose Entity trực tiếp làm form submit chính. Form nhập liệu dùng DTO như `RegisterRequest`, `BusRequest`, `TripRequest`, `BookingRequest`, `TicketLookupRequest`, `ProfileRequest`.
- Controller không chứa business logic.
- Các nghiệp vụ nhiều bảng dùng `@Transactional`.
- Các thao tác nhạy cảm dùng locking.
- Xóa Bus và Trip dùng soft delete để giữ toàn vẹn dữ liệu.
- Ghế thuộc về Trip, không thuộc về Bus.
- Ticket có thể gắn với Seat bằng `@ManyToOne` để cho phép ghế từng bị hủy rồi được đặt lại về sau.

---

## 23. Kết luận

Bus Ticket Pro hoàn thành đầy đủ luồng core của hệ thống đặt vé xe khách liên tỉnh:

```text
Đăng ký -> Đăng nhập -> Tìm chuyến -> Xem ghế -> Đặt vé -> Tra cứu vé -> Staff duyệt vé -> Hủy vé -> Quản lý hồ sơ
```

Điểm mạnh của dự án nằm ở phần xử lý nghiệp vụ thực tế:

- Có phân quyền rõ ràng.
- Có transaction.
- Có lock chống đặt trùng ghế.
- Có giải phóng ghế khi hủy vé.
- Có JOIN dữ liệu đầy đủ khi tra cứu vé.
- Có tổ chức code theo controller, service, repository rõ ràng.
