package com.smartfeedback.dto.admin;

import java.util.List;
import java.util.Map;

public record AdminDashboardResponse(
        List<SubjectAnalyticsResponse> subjectRatings,
        List<FacultyPerformanceResponse> topFaculty,
        List<FacultyPerformanceResponse> lowFaculty,
        long totalFeedback,
        long totalCourses,
        long totalFaculty,
        Map<String, Long> sentimentCount
) {
}
