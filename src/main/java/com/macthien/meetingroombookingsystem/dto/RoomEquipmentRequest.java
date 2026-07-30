package com.macthien.meetingroombookingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomEquipmentRequest {
    @NotNull(message = "ID phòng họp không được để trống")
    private Long roomId;

    @NotNull(message = "ID thiết bị không được để trống")
    private Long equipmentId;

    @NotNull(message = "Số lượng gán không được để trống")
    @Min(value = 1, message = "Số lượng thiết bị gán tối thiểu phải là 1")
    private Integer assignedQuantity;
}
