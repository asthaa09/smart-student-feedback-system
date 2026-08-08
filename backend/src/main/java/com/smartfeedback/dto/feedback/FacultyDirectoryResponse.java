package com.smartfeedback.dto.feedback;

import java.util.List;

public record FacultyDirectoryResponse(
        Long facultyId,
        String facultyName,
        String facultyEmail,
        List<String> subjects
) {
}
