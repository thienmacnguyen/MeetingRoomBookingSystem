package com.macthien.meetingroombookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReportRequest {
    private Long roomId;
    private String roomName;
    private Long bookingCount;
}
