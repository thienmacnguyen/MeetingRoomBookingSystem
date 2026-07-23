package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.DepartmentRequest;
import com.macthien.meetingroombookingsystem.dto.DepartmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface DepartmentService {
    DepartmentResponse createDepartment(DepartmentRequest request) throws DuplicateCodeException;

    DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest request) throws ResourceNotFoundException, DuplicateCodeException;

    Page<DepartmentResponse> getAllDepartments(String search, Pageable pageable) throws ResourceNotFoundException;

    DepartmentResponse getDepartmentById(Long departmentId) throws ResourceNotFoundException;

    void deleteDepartment(Long departmentId) throws ResourceNotFoundException, DeleteConstraintException;
}
