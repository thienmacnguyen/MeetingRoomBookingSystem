package com.macthien.meetingroombookingsystem.entity;

import enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "booking_code", unique = true, nullable = false, length = 50)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private MeetingRoom room;

    @Column(name = "booking_start_time", nullable = false)
    private LocalDateTime bookingStartTime;

    @Column(name = "booking_end_time", nullable = false)
    private LocalDateTime bookingEndTime;

    @Column(name = "booking_purpose", nullable = false, columnDefinition = "TEXT")
    private String bookingPurpose;

    @Column(name = "booking_participants_count", nullable = false)
    private Integer bookingParticipantsCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false, length = 30)
    private BookingStatus bookingStatus;

    @Column(name = "booking_notes", columnDefinition = "TEXT")
    private String bookingNotes;
}
