package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EmployeeRequest;
import com.macthien.meetingroombookingsystem.dto.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request)
            throws DuplicateCodeException, ResourceNotFoundException;

    Page<EmployeeResponse> getAllEmployees(String search, Pageable pageable);

    EmployeeResponse getEmployeeById(Long employeeId)
            throws ResourceNotFoundException;

    EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    void deleteEmployee(Long employeeId)
            throws ResourceNotFoundException;
}
