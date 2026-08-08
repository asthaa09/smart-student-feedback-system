package com.smartfeedback.service;

import com.smartfeedback.dto.feedback.FacultyDirectoryResponse;
import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.User;
import com.smartfeedback.enums.Role;
import com.smartfeedback.exception.ApiException;
import com.smartfeedback.repository.CourseRepository;
import com.smartfeedback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getFacultyCourses(String facultyEmail) {
        User faculty = userRepository.findByEmail(facultyEmail).orElseThrow(() -> new ApiException("Faculty not found"));
        return courseRepository.findByFaculty(faculty);
    }

    public List<FacultyDirectoryResponse> getFacultyDirectory() {
        return userRepository.findByRole(Role.FACULTY).stream()
                .map(faculty -> {
                    List<String> subjects = courseRepository.findByFaculty(faculty).stream()
                            .map(Course::getSubjectCode)
                            .toList();
                    return new FacultyDirectoryResponse(
                            faculty.getId(),
                            faculty.getFullName(),
                            faculty.getEmail(),
                            subjects
                    );
                })
                .toList();
    }
}
