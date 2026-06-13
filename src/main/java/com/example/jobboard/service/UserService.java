package com.example.jobboard.service;
import com.example.jobboard.dto.*;
import com.example.jobboard.exception.BusinessException;
import com.example.jobboard.model.User;
import com.example.jobboard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    public UserService(UserRepository users, PasswordEncoder encoder) { this.users = users; this.encoder = encoder; }

    public User register(RegisterDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new BusinessException("Passwords do not match");
        if (users.existsByUsername(dto.getUsername())) throw new BusinessException("Username already taken");
        if (users.existsByEmail(dto.getEmail())) throw new BusinessException("Email already registered");
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPasswordHash(encoder.encode(dto.getPassword()));
        u.setRole(dto.getRole());
        return users.save(u);
    }
    public User login(LoginDto dto) {
        User u = users.findByUsername(dto.getUsername()).orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!encoder.matches(dto.getPassword(), u.getPasswordHash())) throw new BusinessException("Invalid credentials");
        return u;
    }
    public User getById(java.util.UUID id) {
        return users.findById(id).orElseThrow(() -> new BusinessException("User missing"));
    }
}
