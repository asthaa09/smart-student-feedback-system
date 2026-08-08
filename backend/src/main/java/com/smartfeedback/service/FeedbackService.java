package com.smartfeedback.service;

import com.smartfeedback.dto.feedback.FacultyFeedbackSummaryResponse;
import com.smartfeedback.dto.feedback.CourseFeedbackStatResponse;
import com.smartfeedback.dto.feedback.FeedbackRequest;
import com.smartfeedback.dto.feedback.FeedbackViewResponse;
import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.Feedback;
import com.smartfeedback.entity.User;
import com.smartfeedback.exception.ApiException;
import com.smartfeedback.repository.CourseRepository;
import com.smartfeedback.repository.FeedbackRepository;
import com.smartfeedback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final SentimentService sentimentService;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository, CourseRepository courseRepository, SentimentService sentimentService) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.sentimentService = sentimentService;
    }

    public FeedbackViewResponse submitFeedback(String studentEmail, FeedbackRequest request) {
        User student = userRepository.findByEmail(studentEmail).orElseThrow(() -> new ApiException("Student not found"));
        Course course = courseRepository.findById(request.courseId()).orElseThrow(() -> new ApiException("Course not found"));

        Feedback feedback = new Feedback();
        feedback.setStudent(student);
        feedback.setCourse(course);
        feedback.setCourseRating(request.courseRating());
        feedback.setCourseComment(request.courseComment());
        feedback.setFacultyRating(request.facultyRating());
        feedback.setFacultyComment(request.facultyComment());
        // Backward-compatible fields used by older consumers.
        feedback.setRating(request.facultyRating());
        feedback.setComment(request.facultyComment());
        feedback.setAnonymous(request.anonymous());
        feedback.setSentiment(sentimentService.classify(request.facultyComment()));
        feedback.setSubmittedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);

        return toResponse(feedback);
    }

    public FacultyFeedbackSummaryResponse getFacultyFeedback(String facultyEmail) {
        User faculty = userRepository.findByEmail(facultyEmail).orElseThrow(() -> new ApiException("Faculty not found"));
        List<Course> courses = courseRepository.findByFaculty(faculty);
        List<Feedback> feedbackList = feedbackRepository.findByCourseIn(courses);
        List<FeedbackViewResponse> responses = feedbackList.stream().map(this::toResponse).toList();

        double avg = feedbackList.stream().mapToInt(this::getFacultyRating).average().orElse(0.0);
        Map<String, Long> sentimentCounts = feedbackList.stream()
                .collect(Collectors.groupingBy(f -> f.getSentiment().name(), Collectors.counting()));
        Map<Integer, Long> ratingDistribution = feedbackList.stream()
                .collect(Collectors.groupingBy(this::getFacultyRating, Collectors.counting()));
        List<CourseFeedbackStatResponse> courseStats = courses.stream()
                .map(course -> {
                    List<Feedback> courseFeedback = feedbackList.stream()
                            .filter(feedback -> feedback.getCourse().getId().equals(course.getId()))
                            .toList();
                    double courseAverage = courseFeedback.stream().mapToInt(this::getCourseRating).average().orElse(0.0);
                    return new CourseFeedbackStatResponse(
                            course.getSubjectCode(),
                            course.getSubjectName(),
                            courseAverage,
                            courseFeedback.size()
                    );
                })
                .sorted((a, b) -> Double.compare(b.averageRating(), a.averageRating()))
                .toList();

        return new FacultyFeedbackSummaryResponse(
                avg,
                sentimentCounts,
                ratingDistribution,
                courseStats,
                responses,
                feedbackList.size(),
                courses.size()
        );
    }

    private FeedbackViewResponse toResponse(Feedback feedback) {
        String studentName = feedback.isAnonymous() ? "Anonymous" : feedback.getStudent().getFullName();
        return new FeedbackViewResponse(
                feedback.getId(),
                feedback.getCourse().getSubjectCode(),
                feedback.getCourse().getSubjectName(),
                getCourseRating(feedback),
                feedback.getCourseComment() != null ? feedback.getCourseComment() : feedback.getComment(),
                getFacultyRating(feedback),
                feedback.getFacultyComment() != null ? feedback.getFacultyComment() : feedback.getComment(),
                feedback.isAnonymous(),
                feedback.getCourse().getFaculty().getFullName(),
                studentName,
                feedback.getSentiment(),
                feedback.getSubmittedAt()
        );
    }

    private int getFacultyRating(Feedback feedback) {
        return feedback.getFacultyRating() != null ? feedback.getFacultyRating() : feedback.getRating();
    }

    private int getCourseRating(Feedback feedback) {
        return feedback.getCourseRating() != null ? feedback.getCourseRating() : feedback.getRating();
    }
}
