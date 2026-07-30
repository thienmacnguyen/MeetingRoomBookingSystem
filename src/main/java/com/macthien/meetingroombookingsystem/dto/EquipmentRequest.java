package com.macthien.meetingroombookingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EquipmentRequest {
    @NotBlank(message = "Mã thiết bị không được để trống")
    @Size(max = 50, message = "Mã thiết bị không được vượt quá 50 ký tự")
    private String equipmentCode;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 100, message = "Tên thiết bị không được vượt quá 100 ký tự")
    private String equipmentName;

    @NotBlank(message = "Loại thiết bị không được để trống")
    @Size(max = 50, message = "Loại thiết bị không được vượt quá 50 ký tự")
    private String equipmentType;

    @NotNull(message = "Tổng số lượng trong kho không được để trống")
    @Min(value = 0, message = "Số lượng trong kho không được là số âm")
    private Integer equipmentTotalQuantity;

    private String equipmentCondition;
}
