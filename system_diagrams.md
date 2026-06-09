# Sơ đồ Kiến trúc & Thiết kế Hệ thống - AiTasker

Tài liệu này chứa toàn bộ các sơ đồ UML và ERD mô tả cấu trúc, luồng hoạt động và triển khai của dự án **AiTasker** (Nền Tảng Tìm Kiếm Việc Làm Tự Do Tích Hợp Trợ Lý AI).

---

## 1. Sơ đồ Ca sử dụng (Use Case Diagram)
Sơ đồ ca sử dụng mô tả các tác nhân (Actors) chính trong hệ thống gồm **Client (Nhà tuyển dụng)**, **Expert (Chuyên gia/Freelancer)**, **Admin (Quản trị viên)** và **AI Engine (Hệ thống AI trợ lý)**, cùng các chức năng họ có thể thực hiện.

```mermaid
flowchart LR
    %% Actors
    Client(("Client"))
    Expert(("Expert"))
    Admin(("Admin"))
    AiEngine(("AI Engine"))

    subgraph System ["AiTasker - Nền Tảng Việc Làm Freelancer"]
        %% Common Use Cases
        uc_auth("Đăng ký / Đăng nhập")
        uc_chat("Trò chuyện trực tuyến Realtime")

        %% Client Use Cases
        uc_post_job("Đăng tuyển công việc")
        uc_use_ai_job("AI Job Co-pilot: Chuẩn hóa bài đăng")
        uc_ai_recommend("Nhận gợi ý Expert phù hợp")
        uc_accept_proposal("Duyệt & Chấp nhận Báo giá")
        uc_pay_escrow("Thanh toán ký quỹ Escrow")
        uc_approve_milestone("Nghiệm thu Cột mốc")
        uc_raise_dispute("Gửi yêu cầu tranh chấp")
        uc_review_expert("Đánh giá Expert")

        %% Expert Use Cases
        uc_create_service("Thiết lập gói dịch vụ")
        uc_use_ai_service("AI Service Generator: Sinh mô tả")
        uc_submit_proposal("Gửi báo giá đề xuất")
        uc_submit_milestone("Cập nhật & Nộp kết quả Cột mốc")
        uc_withdraw("Yêu cầu rút tiền")

        %% Admin Use Cases
        uc_manage_user("Quản lý người dùng & Khóa tài khoản")
        uc_resolve_dispute("Hòa giải & Phân xử tranh chấp")
        uc_approve_withdraw("Phê duyệt yêu cầu rút tiền")
        uc_view_analytics("Xem thống kê & Báo cáo hệ thống")
    end

    %% Client associations
    Client --> uc_auth
    Client --> uc_post_job
    Client --> uc_accept_proposal
    Client --> uc_pay_escrow
    Client --> uc_approve_milestone
    Client --> uc_raise_dispute
    Client --> uc_review_expert
    Client --> uc_chat

    %% Expert associations
    Expert --> uc_auth
    Expert --> uc_create_service
    Expert --> uc_submit_proposal
    Expert --> uc_submit_milestone
    Expert --> uc_withdraw
    Expert --> uc_chat

    %% AI relationships
    uc_post_job -.-> uc_use_ai_job
    uc_use_ai_job --- AiEngine
    uc_post_job -.-> uc_ai_recommend
    uc_ai_recommend --- AiEngine
    uc_create_service -.-> uc_use_ai_service
    uc_use_ai_service --- AiEngine

    %% Admin associations
    Admin --> uc_auth
    Admin --> uc_manage_user
    Admin --> uc_resolve_dispute
    Admin --> uc_approve_withdraw
    Admin --> uc_view_analytics
```

---

## 2. Sơ đồ Hoạt động (Activity Diagram)
Sơ đồ hoạt động biểu diễn luồng quy trình nghiệp vụ cốt lõi: Đăng việc -> Đề xuất -> Ký quỹ -> Thực hiện theo Cột mốc -> Nghiệm thu / Tranh chấp -> Rút tiền.

```mermaid
flowchart TD
    Start([Bắt đầu]) --> PostJob["Client đăng bài tuyển dụng & tối ưu bằng AI"]
    PostJob --> AiSuggest["AI phân tích & gợi ý các Expert phù hợp"]
    AiSuggest --> SubmitProposal["Expert gửi báo giá & đề xuất"]
    SubmitProposal --> ReviewProposals["Client duyệt danh sách đề xuất"]
    
    ReviewProposals --> AcceptProposal{"Chấp nhận đề xuất?"}
    AcceptProposal -- "Không" --> ReviewProposals
    AcceptProposal -- "Có" --> CreateProject["Tự động khởi tạo Dự án"]
    
    CreateProject --> PayEscrow["Client thanh toán ký quỹ vào Escrow"]
    PayEscrow --> SetMilestones["Tạo các cột mốc dự án Milestones"]
    
    SetMilestones --> RunMilestone["Expert thực hiện công việc và nộp sản phẩm Cột mốc"]
    RunMilestone --> ClientReview{"Client nghiệm thu?"}
    
    ClientReview -- "Đạt" --> ReleasePayment["Giải ngân tiền ký quỹ của Cột mốc cho Expert"]
    ClientReview -- "Chưa đạt & Cần sửa" --> RunMilestone
    ClientReview -- "Chưa đạt & Tranh chấp" --> RaiseDispute["Gửi yêu cầu tranh chấp lên Admin"]
    
    RaiseDispute --> AdminResolve{"Admin phân xử"}
    AdminResolve -- "Release" --> ReleasePayment
    AdminResolve -- "Refund" --> RefundClient["Hoàn tiền ký quỹ lại cho Client"]
    
    ReleasePayment --> CheckMoreMilestones{"Còn Cột mốc tiếp theo?"}
    RefundClient --> CompleteProject["Hoàn thành / Kết thúc dự án"]
    
    CheckMoreMilestones -- "Còn" --> RunMilestone
    CheckMoreMilestones -- "Hết" --> CompleteProject
    
    CompleteProject --> ReviewEachOther["Hai bên gửi đánh giá Review lẫn nhau"]
    ReviewEachOther --> ExpertWithdraw["Expert gửi yêu cầu rút tiền về ngân hàng"]
    ExpertWithdraw --> AdminApprove{"Admin phê duyệt?"}
    AdminApprove -- "Từ chối" --> RejectWithdraw["Thông báo từ chối & hoàn số dư"]
    AdminApprove -- "Đồng ý" --> Transferred["Chuyển khoản thành công"]
    
    RejectWithdraw --> End([Kết thúc])
    Transferred --> End
```

---

## 3. Sơ đồ Tuần tự (Sequence Diagram)

Phần này mô tả chi tiết hai quy trình tuần tự quan trọng nhất của hệ thống thể hiện sự phối hợp giữa Frontend, Controllers, Services, Repositories và CSDL.

### Quy trình 3.1: Chấp nhận Báo giá & Thanh toán Ký quỹ (Escrow)
Mô tả cách thức Client chấp nhận một đề xuất báo giá (Proposal) để tạo Dự án ([ProjectService.java](file:///d:/study/do%20an/AlTasker/backend/src/main/java/com/aitasker/service/ProjectService.java)) và tiến hành nạp tiền ký quỹ ([PaymentService.java](file:///d:/study/do%20an/AlTasker/backend/src/main/java/com/aitasker/service/PaymentService.java)).

```mermaid
sequenceDiagram
    autonumber
    actor Client as Client (Trình duyệt)
    participant ProjCtrl as ProjectController
    participant ProjServ as ProjectService
    participant ProRepo as ProposalRepository
    participant JobRepo as JobPostRepository
    participant ProjRepo as ProjectRepository
    participant PayCtrl as PaymentController
    participant PayServ as PaymentService
    participant UserRepo as UserRepository
    participant PayRepo as PaymentRepository
    database DB as CSDL MySQL

    Note over Client, DB: Luồng A: Chấp nhận Báo giá & Tự động tạo Dự án
    Client->>ProjCtrl: POST /api/projects/proposal/{proposalId}
    activate ProjCtrl
    ProjCtrl->>ProjServ: createProjectFromProposal(proposalId, clientEmail)
    activate ProjServ
    
    ProjServ->>ProRepo: findById(proposalId)
    ProRepo-->>ProjServ: Trả về Proposal & JobPost tương ứng
    
    ProjServ->>ProRepo: setStatus(ProposalStatus.ACCEPTED)
    ProjServ->>ProRepo: Từ chối các Proposal khác cùng JobPost (REJECTED)
    ProjServ->>JobRepo: setStatus(JobStatus.IN_PROGRESS)
    
    ProjServ->>ProjRepo: save(New Project)
    ProjRepo->>DB: INSERT INTO projects (status=ACTIVE)
    DB-->>ProjRepo: Trả về Project Entity (id)
    
    ProjServ-->>ProjCtrl: Trả về ProjectResponse
    deactivate ProjServ
    ProjCtrl-->>Client: HTTP 201 Created (ProjectResponse)
    deactivate ProjCtrl

    Note over Client, DB: Luồng B: Tạo Thanh toán Ký quỹ (Escrow Payment)
    Client->>PayCtrl: POST /api/payments (projectId, milestoneId, amount, paymentMethod)
    activate PayCtrl
    PayCtrl->>PayServ: createEscrowPayment(clientEmail, PaymentRequest)
    activate PayServ
    
    PayServ->>ProjRepo: findById(projectId)
    ProjRepo-->>PayServ: Trả về Project
    
    PayServ->>PayRepo: save(New Payment)
    PayRepo->>DB: INSERT INTO payments (status=ESCROWED, escrow_status=HELD)
    DB-->>PayRepo: Trả về Payment Entity (id)
    
    PayServ-->>PayCtrl: Trả về PaymentResponse
    deactivate PayServ
    PayCtrl-->>Client: HTTP 201 Created (PaymentResponse)
    deactivate PayCtrl
```

### Quy trình 3.2: Nộp kết quả Cột mốc & Nghiệm thu Giải ngân
Mô tả cách thức Expert nộp kết quả công việc và Client nghiệm thu cột mốc ([MilestoneService.java](file:///d:/study/do%20an/AlTasker/backend/src/main/java/com/aitasker/service/MilestoneService.java)) để giải ngân tiền từ Escrow vào ví số dư của Expert.

```mermaid
sequenceDiagram
    autonumber
    actor Expert as Expert (Freelancer)
    actor Client as Client (Nhà tuyển dụng)
    participant MileCtrl as MilestoneController
    participant MileServ as MilestoneService
    participant MileRepo as MilestoneRepository
    participant PayRepo as PaymentRepository
    participant UserRepo as UserRepository
    database DB as CSDL MySQL

    Note over Expert, DB: Giai đoạn 1: Expert nộp sản phẩm Cột mốc
    Expert->>MileCtrl: PUT /api/milestones/{id}/submit (deliverables)
    activate MileCtrl
    MileCtrl->>MileServ: submitMilestone(id, expertEmail, MilestoneSubmitRequest)
    activate MileServ
    
    MileServ->>MileRepo: findById(id)
    MileRepo-->>MileServ: Trả về Milestone & Project
    
    MileServ->>MileRepo: setStatus(MilestoneStatus.SUBMITTED)
    MileServ->>MileRepo: save(Milestone)
    MileRepo->>DB: UPDATE milestones SET status='SUBMITTED'
    DB-->>MileRepo: Thành công
    
    MileServ-->>MileCtrl: Trả về MilestoneResponse
    deactivate MileServ
    MileCtrl-->>Expert: HTTP 200 OK (MilestoneResponse)
    deactivate MileCtrl

    Note over Client, DB: Giai đoạn 2: Client nghiệm thu & Hệ thống giải ngân tiền ký quỹ
    Client->>MileCtrl: PUT /api/milestones/{id}/approve (feedback)
    activate MileCtrl
    MileCtrl->>MileServ: approveMilestone(id, clientEmail, MilestoneApproveRequest)
    activate MileServ
    
    MileServ->>MileRepo: findById(id)
    MileRepo-->>MileServ: Trả về Milestone
    
    MileServ->>MileRepo: setStatus(MilestoneStatus.APPROVED)
    MileServ->>MileRepo: save(Milestone)
    MileRepo->>DB: UPDATE milestones SET status='APPROVED'
    
    %% Escrow Release logic
    MileServ->>PayRepo: findByProjectId(projectId)
    PayRepo-->>MileServ: Danh sách Payments của Project
    
    Note over MileServ, PayRepo: Tìm Payment liên kết với Milestone và đang có status ESCROWED
    MileServ->>PayRepo: setStatus(PaymentStatus.RELEASED), setEscrowStatus('RELEASED')
    MileServ->>PayRepo: save(Payment)
    PayRepo->>DB: UPDATE payments SET status='RELEASED', escrow_status='RELEASED'
    
    %% Balance updating
    MileServ->>UserRepo: save(Expert với balance tăng thêm amount của payment)
    UserRepo->>DB: UPDATE users SET balance = balance + payment_amount WHERE id = expert_id
    
    MileServ-->>MileCtrl: Trả về MilestoneResponse
    deactivate MileServ
    MileCtrl-->>Client: HTTP 200 OK (MilestoneResponse)
    deactivate MileCtrl
```

---

## 4. Sơ đồ Lớp (Class Diagram)
Sơ đồ biểu diễn cấu trúc các lớp thực thể (Entity) cốt lõi của hệ thống và các mối liên kết giữa chúng.

```mermaid
classDiagram
    direction TB
    
    class User {
        +Long id
        +String email
        +String password
        +String fullName
        +UserRole role
        +Double balance
        +Double rating
        +Boolean isLocked
        +LocalDateTime createdAt
        +onCreate()
        +onUpdate()
    }

    class JobPost {
        +Long id
        +User client
        +String title
        +String description
        +Double budgetMin
        +Double budgetMax
        +JobStatus status
        +JobType type
    }

    class Proposal {
        +Long id
        +JobPost jobPost
        +User expert
        +String coverLetter
        +Double proposedBudget
        +String proposedTimeline
        +ProposalStatus status
    }

    class Project {
        +Long id
        +JobPost jobPost
        +ServiceListing serviceListing
        +User client
        +User expert
        +String title
        +ProjectStatus status
        +Double totalAmount
    }

    class Milestone {
        +Long id
        +Project project
        +String title
        +String description
        +String deliverables
        +String feedback
        +Double amount
        +MilestoneStatus status
    }

    class Payment {
        +Long id
        +Project project
        +Milestone milestone
        +Double amount
        +PaymentStatus status
        +String escrowStatus
    }

    class ServiceListing {
        +Long id
        +User expert
        +String title
        +String description
        +Double price
        +String category
    }

    class Review {
        +Long id
        +Project project
        +User reviewer
        +User reviewee
        +Double rating
        +String comment
    }

    class Dispute {
        +Long id
        +Long projectId
        +String clientName
        +String expertName
        +Double amount
        +String reason
        +String status
    }

    class Withdrawal {
        +Long id
        +User user
        +Double amount
        +String bankName
        +String accountNumber
        +String status
    }

    class Message {
        +Long id
        +Project project
        +User sender
        +String content
        +Boolean isRead
    }

    %% Relationships
    JobPost "0..*" --> "1" User : client
    Proposal "0..*" --> "1" JobPost : jobPost
    Proposal "0..*" --> "1" User : expert
    Project "0..1" --> "0..1" JobPost : jobPost
    Project "0..1" --> "0..1" ServiceListing : serviceListing
    Project "0..*" --> "1" User : client
    Project "0..*" --> "1" User : expert
    Milestone "0..*" --> "1" Project : project
    Payment "0..*" --> "1" Project : project
    Payment "0..1" --> "0..1" Milestone : milestone
    ServiceListing "0..*" --> "1" User : expert
    Review "0..*" --> "1" Project : project
    Review "0..*" --> "1" User : reviewer
    Review "0..*" --> "1" User : reviewee
    Withdrawal "0..*" --> "1" User : user
    Message "0..*" --> "1" Project : project
    Message "0..*" --> "1" User : sender
```

---

## 5. Sơ đồ Thực thể - Mối quan hệ (ERD - Entity Relationship Diagram)
Sơ đồ chi tiết thiết kế Cơ sở dữ liệu MySQL tương ứng với cấu trúc `@Entity` trong Java Spring Boot.

```mermaid
erDiagram
    USERS ||--o{ JOB_POSTS : "places"
    USERS ||--o{ PROPOSALS : "submits"
    USERS ||--o{ PROJECTS : "hires/performs"
    USERS ||--o{ SERVICES : "offers"
    USERS ||--o{ REVIEWS : "writes/receives"
    USERS ||--o{ WITHDRAWALS : "requests"
    USERS ||--o{ MESSAGES : "sends"
    
    JOB_POSTS ||--o{ PROPOSALS : "has"
    JOB_POSTS |o--o| PROJECTS : "generates"
    SERVICES |o--o| PROJECTS : "generates"
    
    PROJECTS ||--o{ MILESTONES : "contains"
    PROJECTS ||--o{ PAYMENTS : "tracks"
    PROJECTS ||--o{ REVIEWS : "evaluated_by"
    PROJECTS ||--o{ MESSAGES : "discusses"
    
    MILESTONES |o--o| PAYMENTS : "linked_to"

    USERS {
        bigint id PK
        varchar email UK
        varchar password
        varchar full_name
        varchar role
        double balance
        double rating
        boolean is_locked
        datetime created_at
        datetime updated_at
    }

    JOB_POSTS {
        bigint id PK
        bigint client_id FK
        varchar title
        text description
        double budget_min
        double budget_max
        varchar timeline
        varchar status
        varchar type
        datetime created_at
    }

    PROPOSALS {
        bigint id PK
        bigint job_post_id FK
        bigint expert_id FK
        text cover_letter
        double proposed_budget
        varchar proposed_timeline
        varchar status
        datetime created_at
    }

    PROJECTS {
        bigint id PK
        bigint job_post_id FK "nullable"
        bigint service_id FK "nullable"
        bigint client_id FK
        bigint expert_id FK
        varchar title
        varchar status
        datetime start_date
        datetime end_date
        double total_amount
    }

    MILESTONES {
        bigint id PK
        bigint project_id FK
        varchar title
        text description
        text deliverables
        text feedback
        double amount
        varchar status
        datetime due_date
    }

    PAYMENTS {
        bigint id PK
        bigint project_id FK
        bigint milestone_id FK "nullable"
        double amount
        varchar status
        varchar payment_method
        varchar escrow_status
        datetime created_at
    }

    SERVICES {
        bigint id PK
        bigint expert_id FK
        varchar title
        text description
        double price
        varchar delivery_time
        varchar category
        datetime created_at
    }

    REVIEWS {
        bigint id PK
        bigint project_id FK
        bigint reviewer_id FK
        bigint reviewee_id FK
        double rating
        text comment
        datetime created_at
    }

    DISPUTES {
        bigint id PK
        bigint project_id
        varchar client_name
        varchar expert_name
        double amount
        text reason
        varchar status
        datetime created_at
    }

    WITHDRAWALS {
        bigint id PK
        bigint user_id FK
        double amount
        varchar bank_name
        varchar account_number
        varchar account_holder_name
        varchar status
        datetime created_at
    }

    MESSAGES {
        bigint id PK
        bigint project_id FK
        bigint sender_id FK
        text content
        boolean is_read
        datetime created_at
    }
```

---

## 6. Sơ đồ Thành phần (Component Diagram)
Sơ đồ biểu diễn các thành phần logic của hệ thống và sự tương tác giữa chúng từ giao diện đến dịch vụ bên ngoài.

```mermaid
flowchart TD
    subgraph Frontend ["Phân hệ Frontend (React SPA + Vite)"]
        LP["Landing Page - Giới thiệu tĩnh"]
        UI["React UI Components"]
        AxiosClient["Axios & Auth Interceptors"]
        WSClient["WebSocket SockJS Client"]
    end

    subgraph Security ["Lớp Bảo Mật"]
        Filter["Spring Security & JWT Filter"]
    end

    subgraph Backend ["Phân hệ Backend (Spring Boot Core)"]
        direction TB
        Ctrl["REST Controllers"]
        WSCtrl["WebSocket Chat Controller"]
        
        Serv["Business Logic Services"]
        AIServ["AiService - AI Co-pilot"]
        
        Repo["JPA Repositories"]
    end

    subgraph Data ["Lớp Lưu Trữ"]
        DB[("MySQL Database")]
    end

    subgraph External ["Dịch vụ Bên Ngoài"]
        GeminiAPI["Google Gemini API"]
    end

    %% Interactions
    LP -.-> UI
    UI --> AxiosClient
    UI --> WSClient

    AxiosClient -- "HTTPS / JSON" --> Filter
    Filter --> Ctrl
    WSClient -- "WSS / STOMP" --> WSCtrl

    Ctrl --> Serv
    WSCtrl --> Serv
    Serv --> AIServ
    Serv --> Repo
    
    AIServ -- "HTTP Request" --> GeminiAPI
    Repo -- "Hibernate SQL" --> DB
```

---

## 7. Sơ đồ Triển khai (Deployment Diagram)
Sơ đồ kiến trúc triển khai phần cứng và các giao thức mạng kết nối giữa các node.

```mermaid
flowchart TD
    subgraph ClientNode ["Thiết bị Người dùng"]
        Browser["Trình duyệt Web"]
    end

    subgraph WebServer ["Web Static Hosting (Vercel / Netlify / Nginx)"]
        FrontendBuild["Static Build: HTML, CSS, JS, Assets"]
    end

    subgraph AppServer ["Máy chủ Ứng dụng (Cloud Run / VPS / AWS EC2)"]
        direction TB
        JVM["Java Virtual Machine - JDK 17"]
        SpringBootApp["AiTasker Backend Application - JAR"]
        EmbeddedTomcat["Embedded Tomcat Web Server"]
        
        TomcatPort["Port 8080"]
    end

    subgraph DbServer ["Máy chủ Cơ sở Dữ liệu"]
        MySQL[("MySQL Server 8.0")]
        DbPort["Port 3306"]
    end

    subgraph ExternalCloud ["Google Cloud Platform"]
        Gemini["Gemini AI Models Engine"]
    end

    %% Network flows
    Browser -- "Tải ứng dụng tĩnh (HTTPS)" --> FrontendBuild
    Browser -- "HTTP REST Requests (Port 8080 - HTTPS)" --> TomcatPort
    Browser -- "WebSocket Connection (ws:// - Port 8080)" --> TomcatPort
    
    TomcatPort --> EmbeddedTomcat
    EmbeddedTomcat --> SpringBootApp
    SpringBootApp --> JVM
    
    SpringBootApp -- "Giao thức JDBC" --> DbPort
    DbPort --> MySQL
    
    SpringBootApp -- "HTTPS API Client" --> Gemini
```
