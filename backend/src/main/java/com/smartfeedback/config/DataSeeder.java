package com.smartfeedback.config;

import com.smartfeedback.entity.Course;
import com.smartfeedback.entity.User;
import com.smartfeedback.enums.Role;
import com.smartfeedback.repository.CourseRepository;
import com.smartfeedback.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@college.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@college.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        User faculty = userRepository.findByEmail("faculty@college.com").orElseGet(() -> {
            User user = new User();
            user.setFullName("Dr. Arjun Mehta");
            user.setEmail("faculty@college.com");
            user.setPassword(passwordEncoder.encode("faculty123"));
            user.setRole(Role.FACULTY);
            return userRepository.save(user);
        });
        if (!"Dr. Arjun Mehta".equals(faculty.getFullName())) {
            faculty.setFullName("Dr. Arjun Mehta");
            userRepository.save(faculty);
        }

        User student = userRepository.findByEmail("student@college.com").orElseGet(() -> {
            User user = new User();
            user.setFullName("Riya Sharma");
            user.setEmail("student@college.com");
            user.setPassword(passwordEncoder.encode("student123"));
            user.setRole(Role.STUDENT);
            return userRepository.save(user);
        });
        if (!"Riya Sharma".equals(student.getFullName())) {
            student.setFullName("Riya Sharma");
            userRepository.save(student);
        }

        if (courseRepository.findAll().isEmpty()) {
            Course c1 = new Course();
            c1.setSubjectCode("CS101");
            c1.setSubjectName("Data Structures");
            c1.setFaculty(faculty);

            Course c2 = new Course();
            c2.setSubjectCode("CS102");
            c2.setSubjectName("Database Systems");
            c2.setFaculty(faculty);

            courseRepository.save(c1);
            courseRepository.save(c2);
        }
    }
}
