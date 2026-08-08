package com.smartfeedback.dto.admin;

public record SubjectAnalyticsResponse(
        String subjectCode,
        String subjectName,
        double averageRating,
        long feedbackCount
) {
}
