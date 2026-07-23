package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.DepartmentRequest;
import com.macthien.meetingroombookingsystem.dto.DepartmentResponse;
import com.macthien.meetingroombookingsystem.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @PostMapping("/create")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentRequest request)
        throws DuplicateCodeException {
        DepartmentResponse response = departmentService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, request));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartment(
            @PathVariable Long departmentId
        ) throws ResourceNotFoundException, DeleteConstraintException {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Xóa phòng ban thành công");
    }

    @GetMapping("search/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(
            @PathVariable Long departmentId
    ) throws ResourceNotFoundException {
        DepartmentResponse response = departmentService.getDepartmentById(departmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/searchall")
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartments(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "departmentId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) throws ResourceNotFoundException {
        // Cấu hình sắp xếp theo thuộc tính truyền vào (mặc định tăng dần theo departmentId)
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DepartmentResponse> response = departmentService.getAllDepartments(search, pageable);
        return ResponseEntity.ok(response);
    }
}

