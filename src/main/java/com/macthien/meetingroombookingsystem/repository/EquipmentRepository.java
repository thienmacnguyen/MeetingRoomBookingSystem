package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByEquipmentIdAndDeletedFalse(Long equipmentId);
    boolean existsByEquipmentCodeAndDeletedFalse(String equipmentCode);
    boolean existsByEquipmentCodeAndEquipmentIdNotAndDeletedFalse(String equipmentCode, Long equipmentId);
    @Query("SELECT e FROM Equipment e WHERE e.deleted = false AND " +
            "(:search IS NULL OR :search = '') OR " +
            "(LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.equipmentType) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Equipment> searchEquipments(@Param("search") String search, Pageable pageable);
}
