package com.aitasker.service;

import com.aitasker.dto.ProjectResponse;
import com.aitasker.dto.ProposalRequest;
import com.aitasker.dto.ProposalResponse;
import com.aitasker.entity.JobPost;
import com.aitasker.entity.Proposal;
import com.aitasker.entity.User;
import com.aitasker.enums.ProposalStatus;
import com.aitasker.enums.UserRole;
import com.aitasker.exception.BadRequestException;
import com.aitasker.exception.ResourceNotFoundException;
import com.aitasker.repository.JobPostRepository;
import com.aitasker.repository.ProposalRepository;
import com.aitasker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    @Transactional
    public ProposalResponse submitProposal(Long jobId, String email, ProposalRequest request) {
        User expert = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (expert.getRole() != UserRole.EXPERT) {
            throw new BadRequestException("Only experts can submit proposals.");
        }

        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPost", "id", jobId));

        Proposal proposal = Proposal.builder()
                .jobPost(jobPost)
                .expert(expert)
                .coverLetter(request.getCoverLetter())
                .proposedBudget(request.getProposedBudget())
                .proposedTimeline(request.getProposedTimeline())
                .status(ProposalStatus.PENDING)
                .build();

        Proposal savedProposal = proposalRepository.save(proposal);
        return ProposalResponse.fromEntity(savedProposal);
    }

    public List<ProposalResponse> getProposalsByJob(Long jobId) {
        return proposalRepository.findByJobPostId(jobId).stream()
                .map(ProposalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProposalResponse> getProposalsByExpert(String email) {
        User expert = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return proposalRepository.findByExpertId(expert.getId()).stream()
                .map(ProposalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProposalResponse acceptProposal(Long proposalId, String clientEmail) {
        // Delegate to project service to start the project and update proposal states
        ProjectResponse projectResponse = projectService.createProjectFromProposal(proposalId, clientEmail);

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal", "id", proposalId));

        ProposalResponse response = ProposalResponse.fromEntity(proposal);
        response.setProjectId(projectResponse.getId());
        return response;
    }

    @Transactional
    public ProposalResponse rejectProposal(Long proposalId, String clientEmail) {
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal", "id", proposalId));

        JobPost jobPost = proposal.getJobPost();
        if (!jobPost.getClient().getEmail().equals(clientEmail)) {
            throw new BadRequestException("You are not authorized to reject proposals for this job.");
        }

        proposal.setStatus(ProposalStatus.REJECTED);
        Proposal updatedProposal = proposalRepository.save(proposal);
        return ProposalResponse.fromEntity(updatedProposal);
    }
}
