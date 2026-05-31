package com.aitasker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpertRecommendationResponse {
    private Long expertId;
    private String fullName;
    private String avatar;
    private Double rating;
    private String skills;
    private Integer suitabilityScore;
    private String reason;
}
