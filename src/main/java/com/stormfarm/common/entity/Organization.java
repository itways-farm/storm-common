package com.stormfarm.common.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "organizations")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Organization {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String domain;
    @Builder.Default
    private java.time.Instant createdAt = java.time.Instant.now();
    @Builder.Default
    private java.time.Instant updatedAt = java.time.Instant.now();
}
