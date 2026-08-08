package com.smartfeedback.controller;

import com.smartfeedback.dto.feedback.FacultyDirectoryResponse;
import com.smartfeedback.dto.feedback.FeedbackRequest;
import com.smartfeedback.dto.feedback.FeedbackViewResponse;
import com.smartfeedback.entity.Course;
import com.smartfeedback.service.CourseService;
import com.smartfeedback.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {
    private final FeedbackService feedbackService;
    private final CourseService courseService;

    public StudentController(FeedbackService feedbackService, CourseService courseService) {
        this.feedbackService = feedbackService;
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> courses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/faculty-directory")
    public ResponseEntity<List<FacultyDirectoryResponse>> facultyDirectory() {
        return ResponseEntity.ok(courseService.getFacultyDirectory());
    }

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackViewResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request, Authentication authentication) {
        return ResponseEntity.ok(feedbackService.submitFeedback(authentication.getName(), request));
    }
}
