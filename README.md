To publish storm-common to JitPack:


git tag common-v1.1.0
git push origin common-v1.1.0



The workflow creates a GitHub Release → JitPack auto-builds it. External consumers then use:

<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
<dependency>
  <groupId>com.github.itways-farm</groupId>
  <artifactId>storm-common</artifactId>
  <version>common-v1.1.0</version>
</dependency>


## What lives here (since common-v1.1.0)

| Package | Contents |
|---|---|
| `com.stormfarm.common.entity` | JPA entities shared by every service (the `stormfarm` schema) |
| `com.stormfarm.common.repository` | The shared repositories (`DeviceRepository`, token repositories). Enable with `@EnableJpaRepositories(basePackages = "com.stormfarm.common.repository")` |
| `com.stormfarm.common.dto` | Request/response DTOs, including `ApiResponse` and `ErrorResponse` |
| `com.stormfarm.common.exception` | The exception hierarchy every service throws (`EntityNotFoundException`, `BadRequestException`, `ConflictException`, ...) |
| `com.stormfarm.common.web` | `CommonExceptionHandler`: the one exception-to-HTTP mapping. Picked up by scan under `com.stormfarm`, otherwise `@Import` it |
| `com.stormfarm.common.security` | JWT service and filter, `BaseSecurityConfig`, `CommonSecurityConfig` |
| `com.stormfarm.common.protocol` | Kafka topic names, Redis channel names, bridge message types, device event names. No string literals for these anywhere else |
| `com.stormfarm.common.event` | In-process events shared by more than one service |

Rules:

- A class goes here only when two or more services need it. Service-specific code stays in the service.
- Never add a `@Service`/`@Component` that a service does not opt into explicitly; configuration beans are wired with `@Import`.
- Run `mvn verify` before tagging: this module has unit tests.
