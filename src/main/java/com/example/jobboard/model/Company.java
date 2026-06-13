package com.example.jobboard.model;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, length = 500) private String description;
    @Column(nullable = false, length = 120) private String website;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false) private User owner;

    public UUID getId() { return id; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getWebsite() { return website; } public void setWebsite(String v) { this.website = v; }
    public User getOwner() { return owner; } public void setOwner(User v) { this.owner = v; }
}
