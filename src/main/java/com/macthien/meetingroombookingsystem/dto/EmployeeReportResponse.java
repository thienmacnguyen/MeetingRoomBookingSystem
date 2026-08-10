package com.macthien.meetingroombookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportResponse {
    private Long employeeId;
    private String employeeFullName;
    private Long bookingCount;
}
