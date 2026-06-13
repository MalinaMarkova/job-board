package com.example.jobboard.dto;
import jakarta.validation.constraints.*;
public class CompanyDto {
    @NotBlank @Size(min = 2, max = 100) private String name;
    @NotBlank @Size(min = 10, max = 500) private String description;
    @NotBlank @Size(max = 120) private String website;
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getWebsite() { return website; } public void setWebsite(String v) { this.website = v; }
}
