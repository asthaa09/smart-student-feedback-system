package com.smartfeedback.repository;

import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.Feedback;
import com.smartfeedback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCourseIn(List<Course> courses);

    List<Feedback> findByCourse(Course course);

    List<Feedback> findByStudent(User student);
}
