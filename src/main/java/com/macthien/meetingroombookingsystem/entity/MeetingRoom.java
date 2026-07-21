package com.macthien.meetingroombookingsystem.entity;

import enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "meeting_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_code", unique = true, nullable = false, length = 50)
    private String roomCode;

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(name = "room_floor", nullable = false)
    private Integer roomFloor;

    @Column(name = "room_capacity", nullable = false)
    private Integer roomCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_status", nullable = false, length = 30)
    private RoomStatus roomStatus;

    @Column(name = "room_description", columnDefinition = "TEXT")
    private String roomDescription;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomEquipment> roomEquipments;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Maintenance> maintenances;
}
