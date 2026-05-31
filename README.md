# 🤖 AiTasker - Nền Tảng Tìm Kiếm Việc Làm Tự Do Tích Hợp Trợ Lý AI

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.2.6-blue.svg)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-8.0.12-purple.svg)](https://vite.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**AiTasker** là một nền tảng tuyển dụng và kết nối việc làm tự do (Freelancer) đột phá, được thiết kế chuyên biệt cho thị trường Việt Nam. Khác biệt với các nền tảng truyền thống, **AiTasker** tích hợp sâu các công nghệ **Trí Tuệ Nhân Tạo (AI)** tiên tiến để tối ưu hóa quy trình làm việc giữa **Client (Nhà tuyển dụng)** và **Expert/Tasker (Chuyên gia/Người nhận việc)**. Dự án được phát triển theo cấu trúc hiện đại gồm 3 phân hệ chính: Trang giới thiệu (Landing Page), Trang ứng dụng khách hàng (Frontend React) và Hệ thống dịch vụ máy chủ (Backend Spring Boot).

---

## 🌟 Tính Năng Nổi Bật

### 1. Phân Hệ Người Dùng & Quản Trị
*   **Trang Giới Thiệu (Landing Page):** Thiết kế giao diện hiện đại, tối ưu hóa SEO, tích hợp hiệu ứng chuyển động mượt mà, giới thiệu đầy đủ các dịch vụ tiêu biểu và quy trình hoạt động của hệ thống.
*   **Quản Lý Tài Khoản & Bảo Mật:** Hệ thống phân quyền chặt chẽ 3 vai trò chính:
    *   **Client (Nhà tuyển dụng):** Đăng tuyển công việc, duyệt báo giá (Proposal), quản lý tiến độ (Milestone), thanh toán ký quỹ, đánh giá Expert.
    *   **Expert/Tasker (Chuyên gia tự do):** Tạo hồ sơ năng lực, thiết lập gói dịch vụ (Service Listings), gửi báo giá cạnh tranh cho các dự án, cập nhật tiến độ công việc, yêu cầu thanh toán và rút tiền về tài khoản ngân hàng.
    *   **Admin (Quản trị viên):** Quản lý toàn bộ danh sách người dùng, phê duyệt và hòa giải tranh chấp (Disputes), kiểm soát dòng tiền ký quỹ, theo dõi thống kê doanh thu và báo cáo hệ thống.

### 2. Trí Tuệ Nhân Tạo (AI Co-pilot) Tích Hợp
*   **AI Job Assistant:** Trợ lý hỗ trợ Client chuẩn hóa bài đăng tuyển dụng. Tự động sửa lỗi chính tả, tối ưu cấu trúc nội dung, gợi ý mức ngân sách phù hợp và tự động gắn thẻ kỹ năng (Skills Tagging).
*   **AI Service Generator:** Giúp Expert tự động sinh mô tả chi tiết, đề xuất lộ trình triển khai công việc và tối ưu hóa từ khóa SEO cho các gói dịch vụ cá nhân dựa trên tiêu đề và mức giá mong muốn.
*   **AI Recommendation System:** Thuật toán thông minh tự động phân tích hồ sơ năng lực, đánh giá kỹ năng của Expert để đề xuất danh sách ứng viên phù hợp nhất ngay khi Client vừa đăng tải công việc.

### 3. Quy Trình Thanh Toán & Giao Dịch An Toàn (Escrow Payment)
*   **Thanh Toán Ký Quỹ (Escrow):** Client thanh toán trước chi phí dự án vào tài khoản trung gian của hệ thống. Tiền chỉ được chuyển cho Expert khi Client xác nhận nghiệm thu kết quả công việc.
*   **Quản Lý Cột Mốc (Milestones):** Hỗ trợ chia dự án thành nhiều giai đoạn nhỏ với các cột mốc thanh toán riêng biệt, giúp giảm thiểu rủi ro cho cả hai bên.
*   **Rút Tiền & Hòa Giải (Withdrawal & Dispute):** Quy trình xử lý yêu cầu rút tiền tự động cho Expert và cơ chế gửi yêu cầu tranh chấp trực tiếp đến Admin khi có bất đồng xảy ra.

### 4. Giao Tiếp Thời Gian Thực (Realtime Messaging)
*   **Hệ Thống Trò Chuyện Trực Tuyến:** Xây dựng trên nền tảng **WebSocket (Stomp/SockJS)** giúp Client và Expert trò chuyện trực tiếp với độ trễ cực thấp.
*   **Đính Kèm Tài Liệu:** Hỗ trợ gửi tệp đính kèm (hồ sơ, báo cáo, sản phẩm demo) trực tiếp qua cửa sổ trò chuyện.

---

## 🛠️ Công Nghệ Sử Dụng

### 💻 Frontend & Landing Page
*   **React 19 & Vite 8:** Khởi tạo dự án siêu tốc, xây dựng SPA (Single Page Application) mượt mà, hiệu năng tối đa.
*   **React Router DOM 7:** Quản lý định tuyến trang chặt chẽ, tối ưu phân quyền trang Dashboard/Admin.
*   **Vanilla CSS:** Thiết kế giao diện Glassmorphism thời thượng, tối ưu responsive 100% trên các thiết bị, sử dụng hệ thống biến CSS chuyên nghiệp.
*   **Axios & JWT Decode:** Giao tiếp API an toàn qua cơ chế Interceptor và lưu trữ mã hóa token.
*   **React Hot Toast:** Hệ thống thông báo người dùng bắt mắt, tương tác trực quan.

### ⚙️ Backend & Database
*   **Java 17 & Spring Boot 3.3.0:** Khung phát triển ứng dụng phía máy chủ mạnh mẽ, bảo mật cao.
*   **Spring Security & JSON Web Token (JWT):** Hệ thống bảo mật Stateful/Stateless xác thực đa tầng, bảo vệ tài nguyên API khỏi các truy cập trái phép.
*   **Spring Data JPA (Hibernate):** Truy vấn và quản trị cơ sở dữ liệu quan hệ tối ưu qua ORM.
*   **Spring WebSocket:** Kết nối hai chiều thời gian thực phục vụ trò chuyện trực tuyến.
*   **MySQL 8.0:** Hệ quản trị cơ sở dữ liệu quan hệ ổn định và an toàn lưu trữ toàn bộ thông tin hệ thống.
*   **Springdoc OpenAPI (Swagger 3):** Tự động sinh tài liệu mô tả đặc tả API phục vụ phát triển và kiểm thử.

---

## 📂 Cấu Trúc Thư Mục Dự Án

```text
AlTasker/
├── backend/                  # Phân hệ Backend (Spring Boot Maven)
│   ├── src/main/java/        # Mã nguồn Java chính
│   │   └── com/aitasker/     # Package gốc chứa các controller, service, entity, dto
│   └── src/main/resources/   # File cấu hình ứng dụng (application.yml, schema sql)
├── frontend/                 # Phân hệ Frontend (React SPA + Vite)
│   ├── src/                  # Mã nguồn React (components, pages, services, assets)
│   └── index.html            # Trang HTML chính của SPA
├── landingpage/              # Phân hệ Landing Page giới thiệu dự án
│   ├── index.html            # File cấu trúc giao diện tĩnh
│   ├── style.css             # Định dạng giao diện độc lập
│   └── script.js             # Logic hoạt động của Landing Page
├── .gitignore                # Quản lý các thư mục/file không đẩy lên Git
└── README.md                 # Tài liệu hướng dẫn đồ án (File này)
```

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Thử Dự Án

### 📋 Yêu Cầu Hệ Thống Trước Khi Cài Đặt
*   **Java Development Kit (JDK):** Phiên bản **17** trở lên.
*   **Node.js:** Phiên bản **18** hoặc **20** trở lên (Khuyến nghị sử dụng LTS).
*   **Cơ Sở Dữ Liệu:** **MySQL Server** phiên bản **8.0** trở lên.
*   **Trình Quản Lý Gói:** **npm** đi kèm với Node.js.

---

### Bước 1: Khởi Tạo Cơ Sở Dữ Liệu MySQL
1. Mở MySQL Workbench hoặc Terminal và chạy câu lệnh sau để tạo database trống:
   ```sql
   CREATE DATABASE aitasker_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Cấu hình thông tin tài khoản MySQL của bạn trong file: `backend/src/main/resources/application.yml` (nếu cần thay đổi tên đăng nhập hoặc mật khẩu mặc định):
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/aitasker_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
       username: root
       password: your_mysql_password
   ```

---

### Bước 2: Khởi Chạy Backend (Spring Boot)
1. Di chuyển vào thư mục backend:
   ```bash
   cd backend
   ```
2. Chạy lệnh để biên dịch và khởi chạy server thông qua Maven:
   ```bash
   mvn spring-boot:run
   ```
3. Sau khi khởi động thành công, hệ thống backend sẽ chạy tại cổng **8080** (`http://localhost:8080`).
4. **Lưu ý quan trọng:** Hệ thống đã được tích hợp sẵn **DataSeeder**. Trong lần chạy đầu tiên khi database trống, hệ thống sẽ tự động gieo dữ liệu demo bao gồm: **4 người dùng mặc định**, **3 bài đăng tuyển**, **2 báo giá**, **2 dự án hoạt động**, **5 cột mốc tiến độ**, **2 giao dịch thanh toán**, và **3 dịch vụ đăng sẵn**.

---

### Bước 3: Khởi Chạy Frontend (React SPA)
1. Di chuyển vào thư mục frontend:
   ```bash
   cd ../frontend
   ```
2. Cài đặt toàn bộ các thư viện dependency cần thiết:
   ```bash
   npm install
   ```
3. Khởi động môi trường phát triển (Development Server):
   ```bash
   npm run dev
   ```
4. Sau khi khởi động thành công, trình duyệt sẽ mở ứng dụng tại cổng **5173** (`http://localhost:5173`).

---

### Bước 4: Khởi Chạy Trang Giới Thiệu (Landing Page)
*   Đối với trang landingpage tĩnh, bạn chỉ cần nhấp đúp trực tiếp vào file `landingpage/index.html` để mở trên trình duyệt hoặc sử dụng extension như **Live Server** trên VS Code để chạy dưới dạng máy chủ cục bộ.

---

## 🔑 Tài Khoản Thử Nghiệm Hệ Thống (Demo Accounts)

Để thuận tiện cho việc kiểm thử và chấm đồ án, hệ thống đã chuẩn bị sẵn các tài khoản demo sau đây:

| 👤 Vai Trò (Role) | ✉️ Địa Chỉ Email | 🔑 Mật Khẩu (Password) | 💡 Ghi Chú |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@aitasker.com` | `123456` | Toàn quyền kiểm soát, quản lý tranh chấp & phê duyệt thanh toán. |
| **Client** | `client@aitasker.com` | `123456` | Đại diện cho Doanh nghiệp / Người thuê dịch vụ AI. |
| **Expert 1** | `expert@aitasker.com` | `123456` | Chuyên gia AI tự do chuyên phát triển Chatbot. |
| **Expert 2** | `expert2@aitasker.com` | `123456` | Chuyên gia AI tự do chuyên về Xử lý Ngôn ngữ Tự nhiên (NLP). |

### 🛠️ Cách truy cập trang Quản Trị (Admin Dashboard):
1. Truy cập `http://localhost:5173/login`.
2. Đăng nhập bằng tài khoản Admin: `admin@aitasker.com` / `123456`.
3. Sau khi đăng nhập thành công, bạn sẽ được tự động chuyển hướng hoặc bạn có thể truy cập trực tiếp địa chỉ: `http://localhost:5173/admin` để kiểm soát dữ liệu.

---

## 🔗 Tài Liệu Tham Khảo API (API Documentation)
Hệ thống sử dụng Swagger UI giúp bạn dễ dàng tra cứu, kiểm thử các API được cung cấp bởi Backend. Khi backend đang chạy, truy cập đường dẫn sau để xem tài liệu API chi tiết:
📌 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

---

## 🛡️ Giấy Phép & Đóng Góp
Dự án được bảo hộ và phân phối dưới giấy phép **MIT License**. Mọi đóng góp xây dựng nâng cấp hệ thống xin vui lòng liên hệ ban phát triển dự án hoặc tạo **Pull Request** trực tiếp trên kho lưu trữ Github.

*Chúc ban giám khảo và quý thầy cô có trải nghiệm tuyệt vời khi chấm điểm dự án!* 🌟
