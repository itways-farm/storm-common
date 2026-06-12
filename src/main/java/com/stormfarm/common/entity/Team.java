package com.stormfarm.common.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "teams")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "organization_id") private Organization organization;
    @Builder.Default
    private java.time.Instant createdAt = java.time.Instant.now();
    @Builder.Default
    private java.time.Instant updatedAt = java.time.Instant.now();
}
