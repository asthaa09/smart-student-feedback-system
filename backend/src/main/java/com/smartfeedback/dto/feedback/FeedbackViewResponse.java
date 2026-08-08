package com.smartfeedback.dto.feedback;

import com.smartfeedback.enums.SentimentType;

import java.time.LocalDateTime;

public record FeedbackViewResponse(
        Long id,
        String subjectCode,
        String subjectName,
        int courseRating,
        String courseComment,
        int facultyRating,
        String facultyComment,
        boolean anonymous,
        String facultyName,
        String studentName,
        SentimentType sentiment,
        LocalDateTime submittedAt
) {
}
