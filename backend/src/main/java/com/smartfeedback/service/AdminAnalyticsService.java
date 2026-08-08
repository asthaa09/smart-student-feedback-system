package com.smartfeedback.service;

import com.smartfeedback.dto.admin.AdminDashboardResponse;
import com.smartfeedback.dto.admin.FacultyPerformanceResponse;
import com.smartfeedback.dto.admin.SubjectAnalyticsResponse;
import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.Feedback;
import com.smartfeedback.entity.User;
import com.smartfeedback.enums.Role;
import com.smartfeedback.repository.CourseRepository;
import com.smartfeedback.repository.FeedbackRepository;
import com.smartfeedback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsService {
    private final CourseRepository courseRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public AdminAnalyticsService(CourseRepository courseRepository, FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    public AdminDashboardResponse getDashboard() {
        List<Course> courses = courseRepository.findAll();
        List<Feedback> feedbackList = feedbackRepository.findAll();
        List<User> facultyUsers = userRepository.findByRole(Role.FACULTY);
        Map<Long, List<Feedback>> feedbackByCourseId = feedbackList.stream()
                .collect(Collectors.groupingBy(f -> f.getCourse().getId()));

        List<SubjectAnalyticsResponse> subjectRatings = courses.stream()
                .map(course -> {
                    List<Feedback> courseFeedback = feedbackByCourseId.getOrDefault(course.getId(), List.of());
                    double avg = courseFeedback.stream().mapToInt(this::getCourseRating).average().orElse(0.0);
                    return new SubjectAnalyticsResponse(
                            course.getSubjectCode(),
                            course.getSubjectName(),
                            avg,
                            courseFeedback.size()
                    );
                })
                .toList();

        Map<User, List<Course>> facultyCourseMap = new ArrayList<>(courses).stream()
                .collect(Collectors.groupingBy(Course::getFaculty));

        List<FacultyPerformanceResponse> facultyPerformance = facultyCourseMap.entrySet().stream()
                .map(entry -> {
                    List<Feedback> f = feedbackRepository.findByCourseIn(entry.getValue());
                    double avg = f.stream().mapToInt(this::getFacultyRating).average().orElse(0.0);
                    return new FacultyPerformanceResponse(
                            entry.getKey().getId(),
                            entry.getKey().getFullName(),
                            avg,
                            f.size(),
                            entry.getValue().size()
                    );
                })
                .sorted(Comparator.comparingDouble(FacultyPerformanceResponse::averageRating).reversed())
                .toList();

        List<FacultyPerformanceResponse> top = facultyPerformance.stream().limit(5).toList();
        List<FacultyPerformanceResponse> low = facultyPerformance.stream()
                .sorted(Comparator.comparingDouble(FacultyPerformanceResponse::averageRating))
                .limit(5)
                .toList();

        Map<String, Long> sentimentCount = feedbackList.stream()
                .collect(Collectors.groupingBy(f -> f.getSentiment().name(), Collectors.counting()));

        return new AdminDashboardResponse(
                subjectRatings,
                top,
                low,
                feedbackList.size(),
                courses.size(),
                facultyUsers.size(),
                sentimentCount
        );
    }

    private int getCourseRating(Feedback feedback) {
        return feedback.getCourseRating() != null ? feedback.getCourseRating() : feedback.getRating();
    }

    private int getFacultyRating(Feedback feedback) {
        return feedback.getFacultyRating() != null ? feedback.getFacultyRating() : feedback.getRating();
    }
}
