package com.macthien.meetingroombookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportRequest {
    private Long employeeId;
    private String employeeFullName;
    private Long bookingCount;
}
