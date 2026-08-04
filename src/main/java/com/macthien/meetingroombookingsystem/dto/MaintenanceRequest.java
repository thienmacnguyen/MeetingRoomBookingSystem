package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.MaintenanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaintenanceRequest {
    @NotNull(message = "ID phòng họp không được để trống")
    private Long roomId;

    @NotNull(message = "Thời gian bắt đầu bảo trì không được để trống")
    private LocalDateTime maintenanceStartTime;

    @NotNull(message = "Thời gian kết thúc bảo trì không được để trống")
    private LocalDateTime maintenanceEndTime;

    @Size(max = 255, message = "Lý do bảo trì không được vượt quá 255 ký tự")
    private String maintenanceReason;

    @NotNull(message = "Trạng thái bảo trì không được để trống")
    private MaintenanceStatus maintenanceStatus;
}
