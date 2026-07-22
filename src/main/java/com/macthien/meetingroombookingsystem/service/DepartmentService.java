package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.dto.DepartmentRequest;
import com.macthien.meetingroombookingsystem.dto.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request);
    Page<DepartmentResponse> getAllDepartments(String search, Pageable pageable);
    DepartmentResponse getDepartmentById(Long id);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    void deleteDepartment(Long id);
}
