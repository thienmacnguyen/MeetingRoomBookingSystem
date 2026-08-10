package com.macthien.meetingroombookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReportResponse {
    private Long roomId;
    private String roomName;
    private Long bookingCount;
}
