package com.macthien.meetingroombookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {
    @NotBlank(message = "Mã phòng ban không được để trống")
    @Size(max = 50, message = "Mã phòng ban không được vượt quá 50 ký tự")
    private String departmentCode;

    @NotBlank(message = "Tên phòng ban không được để trống")
    @Size(max = 100, message = "Tên phòng ban không được vượt quá 100 ký tự")
    private String departmentName;

    private String departmentDescription;

}
