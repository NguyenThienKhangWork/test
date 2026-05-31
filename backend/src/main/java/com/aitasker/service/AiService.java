package com.aitasker.service;

import com.aitasker.dto.*;
import com.aitasker.entity.JobPost;
import com.aitasker.entity.User;
import com.aitasker.enums.UserRole;
import com.aitasker.repository.JobPostRepository;
import com.aitasker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    public JobPostAiResponse improveJobPost(JobPostAiRequest request) {
        String title = request.getTitle() != null ? request.getTitle().toLowerCase() : "";
        String desc = request.getDescription() != null ? request.getDescription().toLowerCase() : "";

        String improvedTitle;
        String improvedDescription;
        Double suggestedBudgetMin;
        Double suggestedBudgetMax;
        String suggestedSkills;

        if (title.contains("rag") || title.contains("langchain") || title.contains("vector") || title.contains("openai") || title.contains("agent")) {
            improvedTitle = "Tích Hợp Hệ Thống Multi-Agent AI & RAG Pipeline Tối Ưu Cho Doanh Nghiệp";
            improvedDescription = "**YÊU CẦU DỰ ÁN (DETAILED SPECIFICATION):**\n" +
                    "Phát triển giải pháp Multi-Agent AI (sử dụng LangChain, AutoGen hoặc CrewAI) kết hợp RAG Pipeline để tự động hóa xử lý và phản hồi thông tin:\n" +
                    "1. Xây dựng Data Extraction Pipeline: Đọc, phân tích và trích xuất dữ liệu từ các định dạng không cấu trúc (PDF, Word, Markdown, Excel).\n" +
                    "2. Lưu trữ tri thức doanh nghiệp vào Vector Database (ChromaDB, Pinecone hoặc Qdrant) sử dụng Embeddings Model tối ưu.\n" +
                    "3. Thiết kế hệ thống tác tử Multi-Agent: Mỗi Agent phụ trách một nhiệm vụ chuyên biệt (Tra cứu dữ liệu, Kiểm tra logic, Giao tiếp API ngoài).\n" +
                    "4. Quản lý hội thoại và Context Window thông minh, đảm bảo phản hồi dưới 1.5 giây.\n\n" +
                    "**CAM KẾT CHẤT LƯỢNG & BÀN GIAO:**\n" +
                    "- Mã nguồn tối ưu, viết Clean Code chuẩn OOP/SOLID.\n" +
                    "- Cung cấp Dockerfile và tài liệu hướng dẫn vận hành hoàn chỉnh.\n" +
                    "- Viết đầy đủ Unit Tests với độ bao phủ >80%.";
            suggestedBudgetMin = 18000000.0;
            suggestedBudgetMax = 38000000.0;
            suggestedSkills = "Python, LangChain, VectorDB, RAG, OpenAI, Agentic AI";
        } else if (title.contains("yolo") || title.contains("vision") || title.contains("camera") || title.contains("image") || title.contains("nhan dien")) {
            improvedTitle = "Xây Dựng Mô Hình Computer Vision Nhận Diện Khuôn Mặt & Camera AI Thời Gian Thực";
            improvedDescription = "**YÊU CẦU DỰ ÁN (DETAILED SPECIFICATION):**\n" +
                    "Triển khai hệ thống Computer Vision thông minh tích hợp trực tiếp với camera giám sát IP RTSP:\n" +
                    "1. Huấn luyện (Fine-tuning) và tối ưu hóa mô hình nhận diện (YOLOv8, YOLOv9 hoặc Faster R-CNN) trên tập dữ liệu đặc thù.\n" +
                    "2. Tích hợp thuật toán tracking đối tượng (DeepSORT, ByteTrack) để phân tích hành vi và theo dõi lộ trình di chuyển.\n" +
                    "3. Xử lý tối ưu hóa phần cứng (sử dụng TensorRT hoặc ONNX) để đạt hiệu năng xử lý >30 FPS trên GPU.\n" +
                    "4. Xây dựng API (FastAPI/Spring Boot) đẩy dữ liệu cảnh báo real-time qua WebSockets.\n\n" +
                    "**CAM KẾT CHẤT LƯỢNG & BÀN GIAO:**\n" +
                    "- Hệ thống chạy ổn định 24/7, tỷ lệ chính xác (Precision) đạt >96%.\n" +
                    "- Dockerize toàn bộ module xử lý Computer Vision.\n" +
                    "- Tài liệu hướng dẫn thiết lập luồng camera chi tiết.";
            suggestedBudgetMin = 15000000.0;
            suggestedBudgetMax = 28000000.0;
            suggestedSkills = "Python, OpenCV, YOLOv8, Computer Vision, PyTorch";
        } else if (title.contains("llm") || title.contains("llama") || title.contains("fine-tuning") || title.contains("nlp") || title.contains("huấn luyện")) {
            improvedTitle = "Fine-Tuning Mô Hình LLM LLaMA-3 Cho Tri Thức Nội Bộ & Tối Ưu Hóa Model Quantization";
            improvedDescription = "**YÊU CẦU DỰ ÁN (DETAILED SPECIFICATION):**\n" +
                    "Thực hiện huấn luyện nâng cao (Fine-tuning) và tối ưu hóa mô hình ngôn ngữ lớn (LLM) để phục vụ cho nghiệp vụ doanh nghiệp:\n" +
                    "1. Thu thập, làm sạch và chuẩn bị tập dữ liệu huấn luyện (Dataset Preparation) đạt chuẩn định dạng Instruct/Chat.\n" +
                    "2. Thực hiện Fine-tuning mô hình (LLaMA-3, Mistral hoặc Qwen) bằng phương pháp QLoRA/LoRA để tiết kiệm tài nguyên GPU.\n" +
                    "3. Nén mô hình (Quantization thành định dạng GGUF/EXL2) để triển khai hiệu năng cao trên máy chủ phổ thông.\n" +
                    "4. Triển khai Local Inference Engine bằng vLLM, Ollama hoặc TGI, kết hợp viết API Endpoint sử dụng.\n\n" +
                    "**CAM KẾT CHẤT LƯỢNG & BÀN GIAO:**\n" +
                    "- Mô hình phản hồi đúng văn phong và nghiệp vụ doanh nghiệp, không xảy ra hiện tượng ảo giác (Hallucination).\n" +
                    "- Bàn giao checkpoint model, script training và hướng dẫn deploy.\n" +
                    "- Cam kết hỗ trợ tích hợp API trong vòng 1 tháng sau bàn giao.";
            suggestedBudgetMin = 22000000.0;
            suggestedBudgetMax = 45000000.0;
            suggestedSkills = "Python, PyTorch, LLaMA, Fine-tuning, NLP, Transformers";
        } else {
            improvedTitle = "Xây Dựng Giải Pháp Trí Tuệ Nhân Tạo (AI) Tối Ưu Hóa Quy Trình Cho Doanh Nghiệp";
            improvedDescription = "**YÊU CẦU DỰ ÁN (DETAILED SPECIFICATION):**\n" +
                    "Nghiên cứu và phát triển giải pháp tích hợp AI nhằm tối ưu hóa hiệu quả hoạt động:\n" +
                    "1. Khảo sát luồng nghiệp vụ hiện tại, đề xuất kiến trúc AI phù hợp (Machine Learning, NLP hoặc Deep Learning).\n" +
                    "2. Xây dựng pipeline xử lý dữ liệu tự động, huấn luyện và đánh giá mô hình trên tập dữ liệu kiểm thử.\n" +
                    "3. Thiết kế kiến trúc RESTful API phục vụ tích hợp trực tiếp vào hệ thống Core hiện tại.\n" +
                    "4. Xây dựng Dashboard báo cáo hiệu suất và trực quan hóa kết quả đầu ra.\n\n" +
                    "**CAM KẾT CHẤT LƯỢNG & BÀN GIAO:**\n" +
                    "- Hệ thống hoạt động chính xác theo KPI đã thỏa thuận.\n" +
                    "- Bàn giao mã nguồn kèm tài liệu API Swagger đầy đủ.\n" +
                    "- Hỗ trợ bảo hành kỹ thuật trong vòng 3 tháng.";
            suggestedBudgetMin = 10000000.0;
            suggestedBudgetMax = 20000000.0;
            suggestedSkills = "Python, Machine Learning, Data Analysis, SQL";
        }

        return JobPostAiResponse.builder()
                .improvedTitle(improvedTitle)
                .improvedDescription(improvedDescription)
                .suggestedBudgetMin(suggestedBudgetMin)
                .suggestedBudgetMax(suggestedBudgetMax)
                .suggestedSkills(suggestedSkills)
                .build();
    }

    public ServiceAiResponse generateServiceDetails(ServiceAiRequest request) {
        String title = request.getTitle() != null ? request.getTitle().toLowerCase() : "";
        String category = request.getCategory() != null ? request.getCategory().toLowerCase() : "";

        String description;
        Double price;
        String deliveryTime;

        if (title.contains("rag") || title.contains("langchain") || title.contains("vector") || title.contains("chatbot")) {
            description = "**MÔ TẢ CHI TIẾT GÓI DỊCH VỤ (SERVICE DESCRIPTION):**\n" +
                    "Đóng gói giải pháp RAG Chatbot thông minh, tự động hóa tra cứu tài liệu nội bộ doanh nghiệp:\n" +
                    "1. Thiết lập CSDL Vector (ChromaDB/Pinecone) và tích hợp OpenAI/Claude API.\n" +
                    "2. Xây dựng giao diện chat trực quan (React/Web/Mobile) kết nối API backend.\n" +
                    "3. Xử lý các file dữ liệu lớn (PDF, Excel, Docx) với thuật toán Chunking & Embedding thông minh, đảm bảo không mất mát ngữ cảnh.\n\n" +
                    "**BẢN BÀN GIAO (DELIVERABLES):**\n" +
                    "- Mã nguồn frontend & backend Dockerized hoàn chỉnh.\n" +
                    "- Tài liệu hướng dẫn sử dụng và cập nhật cơ sở tri thức mới.\n" +
                    "- Tặng 3 tháng bảo trì kỹ thuật miễn phí!";
            price = 15000000.0;
            deliveryTime = "5 ngày";
        } else if (title.contains("yolo") || title.contains("vision") || title.contains("camera") || title.contains("nhận dạng")) {
            description = "**MÔ TẢ CHI TIẾT GÓI DỊCH VỤ (SERVICE DESCRIPTION):**\n" +
                    "Cung cấp gói giải pháp Camera AI giám sát và nhận dạng thông minh sử dụng YOLOv8 tiên tiến:\n" +
                    "1. Tối ưu hóa mô hình nhận dạng vật thể, phát hiện xâm nhập vùng cấm hoặc nhận diện khuôn mặt.\n" +
                    "2. Kết nối trực tiếp luồng camera RTSP thời gian thực với độ trễ thấp (<200ms).\n" +
                    "3. Viết webhook đẩy thông báo cảnh báo tức thời lên Telegram, Slack hoặc hệ thống riêng.\n\n" +
                    "**BẢN BÀN GIAO (DELIVERABLES):**\n" +
                    "- Script xử lý luồng video camera AI tối ưu hóa GPU.\n" +
                    "- Container Docker chạy độc lập.\n" +
                    "- Hỗ trợ cài đặt và deploy trực tiếp lên máy chủ của khách hàng.";
            price = 18000000.0;
            deliveryTime = "7 ngày";
        } else if (title.contains("llm") || title.contains("llama") || title.contains("fine-tune") || title.contains("fine-tuning")) {
            description = "**MÔ TẢ CHI TIẾT GÓI DỊCH VỤ (SERVICE DESCRIPTION):**\n" +
                    "Gói dịch vụ chuyên sâu huấn luyện (Fine-tuning) LLM mã nguồn mở (LLaMA-3, Qwen) phục vụ nghiệp vụ chuyên ngành:\n" +
                    "1. Xây dựng pipeline tiền xử lý và dán nhãn dữ liệu chuẩn hóa.\n" +
                    "2. Tiến hành huấn luyện mô hình bằng kỹ thuật LoRA/QLoRA để đạt chất lượng tối ưu trên phần cứng hợp lý.\n" +
                    "3. Quantization mô hình (nén GGUF/AWQ) và xây dựng API Inference bằng vLLM/Ollama cực kỳ nhanh chóng.\n\n" +
                    "**BẢN BÀN GIAO (DELIVERABLES):**\n" +
                    "- Bộ trọng số mô hình đã huấn luyện (Model Weights).\n" +
                    "- Script huấn luyện và đánh giá chi tiết.\n" +
                    "- Hướng dẫn triển khai RESTful API server.";
            price = 25000000.0;
            deliveryTime = "10 ngày";
        } else {
            description = "**MÔ TẢ CHI TIẾT GÓI DỊCH VỤ (SERVICE DESCRIPTION):**\n" +
                    "Cung cấp giải pháp đóng gói chuyên nghiệp cho dịch vụ: \"" + request.getTitle() + "\".\n" +
                    "Dịch vụ bao gồm trọn gói từ thiết kế kiến trúc hệ thống, phát triển backend API đến triển khai đám mây (Cloud deployment):\n" +
                    "1. Khảo sát yêu cầu, thiết kế giải pháp kỹ thuật tối ưu chi phí.\n" +
                    "2. Phát triển mã nguồn bằng ngôn ngữ lập trình hiệu năng cao (Python/FastAPI/Node.js).\n" +
                    "3. Tích hợp kiểm thử tự động, đảm bảo mã nguồn hoạt động ổn định.\n\n" +
                    "**BẢN BÀN GIAO (DELIVERABLES):**\n" +
                    "- Mã nguồn dự án sạch sẽ, tuân thủ tiêu chuẩn lập trình quốc tế.\n" +
                    "- File cấu hình Docker-compose tiện lợi cho việc triển khai.\n" +
                    "- Hỗ trợ kỹ thuật 24/7 trong 1 tháng đầu tiên.";
            price = 12000000.0;
            deliveryTime = "6 ngày";
        }

        return ServiceAiResponse.builder()
                .description(description)
                .price(price)
                .deliveryTime(deliveryTime)
                .build();
    }

    public List<ExpertRecommendationResponse> recommendExperts(Long jobPostId) {
        JobPost jobPost = jobPostRepository.findById(jobPostId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tin tuyển dụng ID: " + jobPostId));

        List<User> experts = userRepository.findByRole(UserRole.EXPERT);
        List<ExpertRecommendationResponse> recommendations = new ArrayList<>();

        String rawJobSkills = jobPost.getSkillsRequired() != null ? jobPost.getSkillsRequired() : "";
        Set<String> jobSkills = Arrays.stream(rawJobSkills.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        for (User expert : experts) {
            String rawExpertSkills = expert.getSkills() != null ? expert.getSkills() : "";
            Set<String> expertSkills = Arrays.stream(rawExpertSkills.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());

            // Calculate intersection
            Set<String> matchedSkills = new HashSet<>(jobSkills);
            matchedSkills.retainAll(expertSkills);

            int matchCount = matchedSkills.size();
            int totalSkillsRequired = jobSkills.isEmpty() ? 1 : jobSkills.size();

            // Calculate match percentage
            double matchRatio = (double) matchCount / totalSkillsRequired;
            double baseScore = matchRatio * 70.0; // Max 70 points for skill match

            double rating = expert.getRating() != null ? expert.getRating() : 0.0;
            double ratingScore = (rating / 5.0) * 30.0; // Max 30 points for rating

            int suitabilityScore = (int) Math.round(baseScore + ratingScore);
            suitabilityScore = Math.min(100, Math.max(10, suitabilityScore)); // clamp between 10% and 100%

            // Build detailed explanation reason
            String reason;
            if (matchCount > 0) {
                String matchedSkillsStr = matchedSkills.stream()
                        .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)) // Capitalize
                        .collect(Collectors.joining(", "));
                
                reason = String.format("Chuyên gia AI xuất sắc sở hữu %d kỹ năng trùng khớp bao gồm [%s]. Điểm đánh giá kỹ thuật đạt mức tuyệt đối %s⭐ và có kinh nghiệm thực chiến phong phú trong lĩnh vực này.",
                        matchCount, matchedSkillsStr, String.format(Locale.US, "%.1f", rating));
            } else {
                reason = String.format("Chuyên gia AI đa năng với điểm đánh giá kỹ thuật %s⭐, sở hữu tư duy thuật toán xuất sắc, sẵn sàng nghiên cứu các công nghệ mới và tiếp cận nghiệp vụ của dự án một cách nhanh chóng nhất.",
                        String.format(Locale.US, "%.1f", rating));
            }

            // If there's certifications, add a note
            if (expert.getCertifications() != null && !expert.getCertifications().trim().isEmpty()) {
                reason += " Đã xác thực các chứng chỉ uy tín: " + expert.getCertifications() + ".";
            }

            recommendations.add(ExpertRecommendationResponse.builder()
                    .expertId(expert.getId())
                    .fullName(expert.getFullName())
                    .avatar(expert.getAvatar() != null ? expert.getAvatar() : "👨‍💻")
                    .rating(rating)
                    .skills(expert.getSkills())
                    .suitabilityScore(suitabilityScore)
                    .reason(reason)
                    .build());
        }

        // Sort by suitability score descending
        recommendations.sort((r1, r2) -> r2.getSuitabilityScore().compareTo(r1.getSuitabilityScore()));

        return recommendations;
    }
}
