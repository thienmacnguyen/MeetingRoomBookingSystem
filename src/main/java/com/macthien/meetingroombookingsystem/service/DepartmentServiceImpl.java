package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.dto.DepartmentRequest;
import com.macthien.meetingroombookingsystem.dto.DepartmentResponse;
import com.macthien.meetingroombookingsystem.repository.DepartmentRepository;
import com.macthien.meetingroombookingsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        return null;
    }

    @Override
    public Page<DepartmentResponse> getAllDepartments(String search, Pageable pageable) {
        return null;
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        return null;
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        return null;
    }

    @Override
    public void deleteDepartment(Long id) {

    }
}
