package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String bookingCode;
    private Long employeeId;
    private String employeeFullName;
    private Long roomId;
    private String roomName;
    private LocalDateTime bookingStartTime;
    private LocalDateTime bookingEndTime;
    private String bookingPurpose;
    private Integer bookingParticipantsCount;
    private BookingStatus bookingStatus;
    private String bookingNotes;
}
