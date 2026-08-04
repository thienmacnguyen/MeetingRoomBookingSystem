package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.MaintenanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {
    private Long maintenanceId;
    private Long roomId;
    private String roomName;
    private LocalDateTime maintenanceStartTime;
    private LocalDateTime maintenanceEndTime;
    private String maintenanceReason;
    private MaintenanceStatus maintenanceStatus;
}
