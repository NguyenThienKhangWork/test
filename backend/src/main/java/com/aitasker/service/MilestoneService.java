package com.aitasker.service;

import com.aitasker.dto.MilestoneRequest;
import com.aitasker.dto.MilestoneResponse;
import com.aitasker.dto.MilestoneSubmitRequest;
import com.aitasker.dto.MilestoneApproveRequest;
import com.aitasker.dto.StatusChangeRequest;
import com.aitasker.entity.Dispute;
import com.aitasker.entity.Milestone;
import com.aitasker.entity.Payment;
import com.aitasker.entity.Project;
import com.aitasker.enums.MilestoneStatus;
import com.aitasker.enums.PaymentStatus;
import com.aitasker.enums.ProjectStatus;
import com.aitasker.exception.BadRequestException;
import com.aitasker.exception.ResourceNotFoundException;
import com.aitasker.repository.DisputeRepository;
import com.aitasker.repository.MilestoneRepository;
import com.aitasker.repository.PaymentRepository;
import com.aitasker.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final PaymentRepository paymentRepository;
    private final DisputeRepository disputeRepository;
    private final PaymentService paymentService;

    @Transactional
    public MilestoneResponse createMilestone(Long projectId, String clientEmail, MilestoneRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        if (!project.getClient().getEmail().equals(clientEmail)) {
            throw new BadRequestException("Only the client can create project milestones.");
        }

        Milestone milestone = Milestone.builder()
                .project(project)
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .dueDate(request.getDueDate())
                .status(MilestoneStatus.PENDING)
                .build();

        Milestone saved = milestoneRepository.save(milestone);
        return MilestoneResponse.fromEntity(saved);
    }

    public List<MilestoneResponse> getMilestonesByProject(Long projectId, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        if (!project.getClient().getEmail().equals(email) && !project.getExpert().getEmail().equals(email)) {
            throw new BadRequestException("You are not authorized to view this project's milestones.");
        }

        return milestoneRepository.findByProjectId(projectId).stream()
                .map(MilestoneResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MilestoneResponse submitMilestone(Long milestoneId, String expertEmail, MilestoneSubmitRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Project project = milestone.getProject();
        if (!project.getExpert().getEmail().equals(expertEmail)) {
            throw new BadRequestException("Only the project expert can submit milestone work.");
        }

        if (milestone.getStatus() != MilestoneStatus.PENDING
                && milestone.getStatus() != MilestoneStatus.REVISION_REQUESTED) {
            throw new BadRequestException("Milestone must be PENDING or REVISION_REQUESTED before submission.");
        }

        milestone.setDeliverables(request.getDeliverables());
        milestone.setStatus(MilestoneStatus.SUBMITTED);
        Milestone saved = milestoneRepository.save(milestone);
        return MilestoneResponse.fromEntity(saved);
    }

    @Transactional
    public MilestoneResponse approveMilestone(Long milestoneId, String clientEmail, MilestoneApproveRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Project project = milestone.getProject();
        if (!project.getClient().getEmail().equals(clientEmail)) {
            throw new BadRequestException("Only the project client can approve milestone work.");
        }

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED) {
            throw new BadRequestException("Milestone must be in SUBMITTED state to be approved.");
        }

        milestone.setFeedback(request.getFeedback());
        milestone.setStatus(MilestoneStatus.APPROVED);
        Milestone saved = milestoneRepository.save(milestone);

        // Delegate escrow release to Dev 4 PaymentService instead of duplicating payment logic here.
        List<Payment> payments = paymentRepository.findByMilestoneId(milestoneId);
        for (Payment payment : payments) {
            if (payment.getStatus() == PaymentStatus.ESCROWED) {
                paymentService.releaseEscrowPayment(payment.getId(), clientEmail);
            }
        }

        return MilestoneResponse.fromEntity(saved);
    }

    @Transactional
    public MilestoneResponse requestRevision(Long milestoneId, String clientEmail, StatusChangeRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Project project = milestone.getProject();
        if (!project.getClient().getEmail().equals(clientEmail)) {
            throw new BadRequestException("Only the project client can request milestone revisions.");
        }

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED) {
            throw new BadRequestException("Milestone must be SUBMITTED before requesting revisions.");
        }

        milestone.setFeedback(resolveReason(request, "Revision requested by client."));
        milestone.setStatus(MilestoneStatus.REVISION_REQUESTED);
        return MilestoneResponse.fromEntity(milestoneRepository.save(milestone));
    }

    @Transactional
    public MilestoneResponse disputeMilestone(Long milestoneId, String requesterEmail, StatusChangeRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        Project project = milestone.getProject();
        if (!project.getClient().getEmail().equals(requesterEmail)
                && !project.getExpert().getEmail().equals(requesterEmail)) {
            throw new BadRequestException("Only project participants can dispute a milestone.");
        }

        if (milestone.getStatus() == MilestoneStatus.APPROVED) {
            throw new BadRequestException("Approved milestones cannot be disputed.");
        }

        String reason = resolveReason(request, "Milestone dispute requested by a project participant.");
        milestone.setFeedback(reason);
        milestone.setStatus(MilestoneStatus.DISPUTED);
        project.setStatus(ProjectStatus.DISPUTED);
        projectRepository.save(project);
        createDispute(project, milestone, reason);

        return MilestoneResponse.fromEntity(milestoneRepository.save(milestone));
    }

    private String resolveReason(StatusChangeRequest request, String fallback) {
        if (request == null) {
            return fallback;
        }
        if (request.getReason() != null && !request.getReason().isBlank()) {
            return request.getReason();
        }
        if (request.getFeedback() != null && !request.getFeedback().isBlank()) {
            return request.getFeedback();
        }
        return fallback;
    }

    private void createDispute(Project project, Milestone milestone, String reason) {
        Dispute dispute = Dispute.builder()
                .projectId(project.getId())
                .clientName(project.getClient().getFullName())
                .expertName(project.getExpert().getFullName())
                .title("Milestone #" + milestone.getId() + ": " + milestone.getTitle())
                .amount(milestone.getAmount() != null ? milestone.getAmount() : 0.0)
                .reason(reason)
                .status("PENDING")
                .build();
        disputeRepository.save(dispute);
    }
}
