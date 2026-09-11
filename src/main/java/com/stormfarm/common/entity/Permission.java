package com.stormfarm.common.entity;

import jakarta.persistence.*;

/**
 * One capability a role may hold, e.g. {@code devices:manage}.
 *
 * The catalogue itself lives in storm-core's Permission enum, which is the
 * source of truth; these rows are that catalogue persisted so roles can
 * reference them. Category and description exist so the console can present a
 * grouped, self-explaining grid rather than a wall of identifiers.
 */
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Stable key used in authorization checks and in the token. */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Groups the permission in the console's grid, e.g. "Devices". */
    @Column(length = 50)
    private String category;

    /** One line explaining the capability in the reader's terms. */
    @Column(length = 255)
    private String description;

    public Permission() {}

    public Permission(String name) { this.name = name; }

    public Permission(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission other)) return false;
        return name != null && name.equals(other.name);
    }

    @Override public int hashCode() { return name == null ? 0 : name.hashCode(); }

    @Override public String toString() { return name; }
}
