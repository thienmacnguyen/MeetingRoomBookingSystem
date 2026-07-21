package com.macthien.meetingroombookingsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "equipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(name = "equipment_code", unique = true, nullable = false, length = 50)
    private String equipmentCode;

    @Column(name = "equipment_name", nullable = false, length = 100)
    private String equipmentName;

    @Column(name = "equipment_type", nullable = false, length = 50)
    private String equipmentType;

    @Column(name = "equipment_total_quantity", nullable = false)
    private Integer equipmentTotalQuantity;

    @Column(name = "equipment_condition", length = 100)
    private String equipmentCondition;

    @OneToMany(mappedBy = "equipment", fetch = FetchType.LAZY)
    private List<RoomEquipment> roomEquipments;
}
