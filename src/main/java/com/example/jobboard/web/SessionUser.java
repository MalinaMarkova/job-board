package com.example.jobboard.web;
import com.example.jobboard.model.Role;
import java.util.UUID;
public class SessionUser {
    public static final String KEY = "SESSION_USER";
    private final UUID userId;
    private final String username;
    private final Role role;
    public SessionUser(UUID userId, String username, Role role) {
        this.userId = userId; this.username = username; this.role = role;
    }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
}
