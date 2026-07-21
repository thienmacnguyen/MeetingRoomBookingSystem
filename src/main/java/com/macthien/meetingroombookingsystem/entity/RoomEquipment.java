package com.macthien.meetingroombookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "room_equipments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "equipment_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_equipment_id")
    private Long roomEquipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private MeetingRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(name = "assigned_quantity", nullable = false)
    private Integer assignedQuantity;
}
