package com.smartfeedback.entity;

import com.smartfeedback.enums.SentimentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 2000)
    private String comment;

    @Column(nullable = false)
    private Integer courseRating;

    @Column(nullable = false, length = 2000)
    private String courseComment;

    @Column(nullable = false)
    private Integer facultyRating;

    @Column(nullable = false, length = 2000)
    private String facultyComment;

    @Column(nullable = false)
    private boolean anonymous;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SentimentType sentiment;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(Integer courseRating) {
        this.courseRating = courseRating;
    }

    public String getCourseComment() {
        return courseComment;
    }

    public void setCourseComment(String courseComment) {
        this.courseComment = courseComment;
    }

    public Integer getFacultyRating() {
        return facultyRating;
    }

    public void setFacultyRating(Integer facultyRating) {
        this.facultyRating = facultyRating;
    }

    public String getFacultyComment() {
        return facultyComment;
    }

    public void setFacultyComment(String facultyComment) {
        this.facultyComment = facultyComment;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public SentimentType getSentiment() {
        return sentiment;
    }

    public void setSentiment(SentimentType sentiment) {
        this.sentiment = sentiment;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
