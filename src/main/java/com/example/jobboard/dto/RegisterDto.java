package com.example.jobboard.dto;
import com.example.jobboard.model.Role;
import jakarta.validation.constraints.*;
public class RegisterDto {
    @NotBlank @Size(min = 3, max = 50) private String username;
    @NotBlank @Email @Size(max = 120) private String email;
    @NotBlank @Size(min = 6, max = 100) private String password;
    @NotBlank private String confirmPassword;
    @NotNull private Role role;
    public String getUsername() { return username; } public void setUsername(String v) { this.username = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
    public String getPassword() { return password; } public void setPassword(String v) { this.password = v; }
    public String getConfirmPassword() { return confirmPassword; } public void setConfirmPassword(String v) { this.confirmPassword = v; }
    public Role getRole() { return role; } public void setRole(Role v) { this.role = v; }
}
