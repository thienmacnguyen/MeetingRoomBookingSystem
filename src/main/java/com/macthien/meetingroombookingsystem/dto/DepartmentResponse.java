package com.macthien.meetingroombookingsystem.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private String departmentDescription;
}
