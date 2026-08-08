package com.smartfeedback.controller;

import com.smartfeedback.dto.feedback.FacultyFeedbackSummaryResponse;
import com.smartfeedback.entity.Course;
import com.smartfeedback.service.CourseService;
import com.smartfeedback.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyController {
    private final FeedbackService feedbackService;
    private final CourseService courseService;

    public FacultyController(FeedbackService feedbackService, CourseService courseService) {
        this.feedbackService = feedbackService;
        this.courseService = courseService;
    }

    @GetMapping("/feedback")
    public ResponseEntity<FacultyFeedbackSummaryResponse> myFeedback(Authentication authentication) {
        return ResponseEntity.ok(feedbackService.getFacultyFeedback(authentication.getName()));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> myCourses(Authentication authentication) {
        return ResponseEntity.ok(courseService.getFacultyCourses(authentication.getName()));
    }
}
