package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EmployeeRequest;
import com.macthien.meetingroombookingsystem.dto.EmployeeResponse;
import com.macthien.meetingroombookingsystem.service.EmployeeService;
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
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/create")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request)
            throws DuplicateCodeException, ResourceNotFoundException {
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("id") Long employeeId)
            throws ResourceNotFoundException {
        EmployeeResponse response = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "employeeId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmployeeResponse> response = employeeService.getAllEmployees(search, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable("id") Long employeeId,
            @Valid @RequestBody EmployeeRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        EmployeeResponse response = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteEmployee(@PathVariable("id") Long employeeId)
            throws ResourceNotFoundException {
        employeeService.softDeleteEmployee(employeeId);
        return ResponseEntity.ok("Đã xóa nhân viên thành công.");
    }

    @DeleteMapping("/purge/{id}")
    public ResponseEntity<String> hardDeleteEmployee(@PathVariable("id") Long employeeId)
            throws ResourceNotFoundException {
        employeeService.hardDeleteEmployee(employeeId);
        return ResponseEntity.ok("Đã xóa nhân viên thành công.");
    }

}
