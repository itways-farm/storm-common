package com.stormfarm.common.entity;

import com.stormfarm.common.entity.enums.AccessScope;
import jakarta.persistence.*;
import lombok.*;

/**
 * A named bundle of permissions plus the reach those permissions carry.
 *
 * Roles are data, not code: an administrator can create one from the console and
 * it takes effect without a release. The two roles the platform ships with are
 * marked {@code systemRole} so they cannot be deleted out from under the
 * installation, though their permissions and scope remain editable.
 */
@Entity
@Table(name = "roles")
// Identity is the role name, not every field. Lombok's default equals would
// compare the permissions collection too, so a Set<Role> stopped recognising a
// role it already held once roles actually had permissions — which produced a
// duplicate key when granting someone a role they were already given.
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    /** How far this role can see. See {@link AccessScope}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 20)
    @Builder.Default
    private AccessScope scope = AccessScope.OWN;

    /**
     * Shipped with the platform. Such a role cannot be renamed or deleted, so an
     * installation always retains a way in; its permissions and scope stay editable.
     */
    @Column(name = "system_role", nullable = false)
    @Builder.Default
    private boolean systemRole = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Builder.Default
    private java.util.Set<Permission> permissions = new java.util.HashSet<>();

    /** Permission keys held by this role, e.g. {@code devices:manage}. */
    public java.util.Set<String> permissionKeys() {
        return permissions.stream().map(Permission::getName).collect(java.util.stream.Collectors.toSet());
    }
}
