package com.smartfeedback.dto.feedback;

public record CourseFeedbackStatResponse(
        String subjectCode,
        String subjectName,
        double averageRating,
        long feedbackCount
) {
}
