package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long employeeId;
    private String employeeCode;
    private String employeeFullName;
    private String employeeEmail;
    private String employeePhone;
    private Long departmentId;
    private String departmentName;
    private EmployeeStatus employeeStatus;
}
