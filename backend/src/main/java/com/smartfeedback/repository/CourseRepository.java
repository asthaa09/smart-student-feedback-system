package com.smartfeedback.repository;

import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByFaculty(User faculty);
}
