package com.smartfeedback.dto.auth;

import com.smartfeedback.enums.Role;

public record AuthResponse(
        String token,
        String fullName,
        String email,
        Role role
) {
}
