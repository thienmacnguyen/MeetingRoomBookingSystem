package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Maintenance;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    Optional<Maintenance> findByMaintenanceIdAndDeletedFalse(Long maintenanceId);
    @Query("SELECT m FROM Maintenance m WHERE m.deleted = false AND " +
            "(:search IS NULL OR :search = '') OR " +
            "LOWER(m.room.roomName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.maintenanceReason) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Maintenance> searchMaintenances(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(m) > 0 FROM Maintenance m " +
            "WHERE m.deleted = false " +
            "AND m.room.roomId = :roomId " +
            "AND m.maintenanceStatus <> :cancelledStatus " +
            "AND m.maintenanceStartTime < :endTime " +
            "AND m.maintenanceEndTime > :startTime")
    boolean existsOverlapMaintenance(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );
}
