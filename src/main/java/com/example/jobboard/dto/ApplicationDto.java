package com.example.jobboard.dto;
import jakarta.validation.constraints.*;
public class ApplicationDto {
    @NotBlank @Size(min = 20, max = 1500) private String coverLetter;
    public String getCoverLetter() { return coverLetter; } public void setCoverLetter(String v) { this.coverLetter = v; }
}
