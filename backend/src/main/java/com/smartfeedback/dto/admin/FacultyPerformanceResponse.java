package com.smartfeedback.dto.admin;

public record FacultyPerformanceResponse(
        Long facultyId,
        String facultyName,
        double averageRating,
        long feedbackCount,
        long courseCount
) {
}
