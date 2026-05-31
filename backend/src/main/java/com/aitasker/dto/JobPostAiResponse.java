package com.aitasker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostAiResponse {
    private String improvedTitle;
    private String improvedDescription;
    private Double suggestedBudgetMin;
    private Double suggestedBudgetMax;
    private String suggestedSkills;
}
