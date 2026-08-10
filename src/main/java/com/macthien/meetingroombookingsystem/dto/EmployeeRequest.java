package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    @NotBlank(message = "Mã nhân viên không được để trống")
    @Size(max = 50, message = "Mã nhân viên không được vượt quá 50 ký tự")
    private String employeeCode;

    @NotBlank(message = "Họ tên nhân viên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String employeeFullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String employeeEmail;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Số điện thoại không đúng định dạng")
    private String employeePhone;

    @NotNull(message = "ID phòng ban không được để trống")
    private Long departmentId;

    @NotNull(message = "Trạng thái nhân viên không được để trống")
    private EmployeeStatus employeeStatus;
}
