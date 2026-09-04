package com.stormfarm.common.repository;

import com.stormfarm.common.entity.Device;
import com.stormfarm.common.entity.enums.DevicePlatform;
import com.stormfarm.common.entity.enums.DeviceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The one {@link Device} repository. Every service that reads the shared
 * {@code devices} table uses this interface; enable it with
 * {@code @EnableJpaRepositories(basePackages = "com.stormfarm.common.repository")}.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findBySerial(String serial);

    Optional<Device> findByStfSerial(String stfSerial);

    List<Device> findBySerialIn(Collection<String> serials);

    @Query("SELECT d FROM Device d WHERE " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:platform IS NULL OR d.platform = :#{#platform?.name()}) AND " +
           "(:search IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', cast(:search as string), '%')) OR LOWER(d.model) LIKE LOWER(CONCAT('%', cast(:search as string), '%')))")
    Page<Device> findFiltered(@Param("status") DeviceStatus status,
                               @Param("platform") DevicePlatform platform,
                               @Param("search") String search,
                               Pageable pageable);

    @Query("SELECT d FROM Device d WHERE " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:platform IS NULL OR d.platform = :#{#platform?.name()}) AND " +
           "(:brand IS NULL OR LOWER(d.model) LIKE LOWER(CONCAT(cast(:brand as string), '%'))) AND " +
           "(:search IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', cast(:search as string), '%')) OR LOWER(d.model) LIKE LOWER(CONCAT('%', cast(:search as string), '%')))")
    Page<Device> findFilteredWithBrand(@Param("status") DeviceStatus status,
                                        @Param("platform") DevicePlatform platform,
                                        @Param("brand") String brand,
                                        @Param("search") String search,
                                        Pageable pageable);
}
