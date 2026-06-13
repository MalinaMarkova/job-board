package com.example.jobboard.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
public class JobPostDto {
    @NotBlank @Size(min = 3, max = 120) private String title;
    @NotBlank @Size(min = 20, max = 2000) private String description;
    @NotBlank @Size(min = 2, max = 80) private String location;
    @NotNull @DecimalMin("0.0") @DecimalMax("10000000.0") private BigDecimal salary;
    @NotNull private UUID companyId;
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getLocation() { return location; } public void setLocation(String v) { this.location = v; }
    public BigDecimal getSalary() { return salary; } public void setSalary(BigDecimal v) { this.salary = v; }
    public UUID getCompanyId() { return companyId; } public void setCompanyId(UUID v) { this.companyId = v; }
}
