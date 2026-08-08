package com.smartfeedback.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(
        @NotNull Long courseId,
        @NotNull @Min(1) @Max(5) Integer courseRating,
        @NotBlank String courseComment,
        @NotNull @Min(1) @Max(5) Integer facultyRating,
        @NotBlank String facultyComment,
        boolean anonymous
) {
}
