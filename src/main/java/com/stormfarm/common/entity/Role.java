package com.stormfarm.common.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "roles")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private java.util.Set<Permission> permissions = new java.util.HashSet<>();
}
