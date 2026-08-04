package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.RoomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomEquipmentRepository extends JpaRepository<RoomEquipment, Long> {
    boolean existsByRoomRoomIdAndEquipmentEquipmentId(Long roomId, Long equipmentId);
    Optional<RoomEquipment> findByRoomRoomIdAndEquipmentEquipmentId(Long roomId, Long equipmentId);
    List<RoomEquipment> findByRoomRoomId(Long roomId);
    boolean existsByEquipmentEquipmentId(Long equipmentId);
    //COALESCE(SUM( nếu sum bằng null trả về 0
    @Query("""
    SELECT COALESCE(SUM(re.assignedQuantity), 0)
    FROM RoomEquipment re
    WHERE re.equipment.equipmentId = :equipmentId
""")
    Integer getTotalAssignedQuantity(@Param("equipmentId") Long equipmentId);
}
