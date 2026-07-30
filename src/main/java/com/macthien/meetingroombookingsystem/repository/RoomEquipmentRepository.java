package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.RoomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomEquipmentRepository extends JpaRepository<RoomEquipment, Long> {
    boolean existsByRoomRoomIdAndEquipmentEquipmentId(Long roomId, Long equipmentId);
    Optional<RoomEquipment> findByRoomRoomIdAndEquipmentEquipmentId(Long roomId, Long equipmentId);
    List<RoomEquipment> findByRoomRoomId(Long roomId);
    boolean existsByEquipmentEquipmentId(Long equipmentId);
}
