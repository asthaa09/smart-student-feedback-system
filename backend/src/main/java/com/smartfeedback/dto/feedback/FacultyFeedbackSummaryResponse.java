package com.smartfeedback.dto.feedback;

import java.util.List;
import java.util.Map;

public record FacultyFeedbackSummaryResponse(
        double averageRating,
        Map<String, Long> sentimentCount,
        Map<Integer, Long> ratingDistribution,
        List<CourseFeedbackStatResponse> courseStats,
        List<FeedbackViewResponse> feedback,
        long totalFeedback,
        long totalCourses
) {
}
