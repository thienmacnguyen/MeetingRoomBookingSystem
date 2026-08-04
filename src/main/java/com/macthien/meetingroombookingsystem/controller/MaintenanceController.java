package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.MaintenanceRequest;
import com.macthien.meetingroombookingsystem.dto.MaintenanceResponse;
import com.macthien.meetingroombookingsystem.service.MaintenanceService;
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
@RequestMapping("/api/maintenances")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping("/create")
    public ResponseEntity<MaintenanceResponse> createMaintenance(@Valid @RequestBody MaintenanceRequest request)
            throws ResourceNotFoundException, DuplicateCodeException {
        MaintenanceResponse response = maintenanceService.createMaintenance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<MaintenanceResponse>> getAllMaintenances(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "maintenanceId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MaintenanceResponse> response = maintenanceService.getAllMaintenances(search, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> getMaintenanceById(@PathVariable("id") Long maintenanceId)
            throws ResourceNotFoundException {
        MaintenanceResponse response = maintenanceService.getMaintenanceById(maintenanceId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MaintenanceResponse> updateMaintenance(
            @PathVariable("id") Long maintenanceId,
            @Valid @RequestBody MaintenanceRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        MaintenanceResponse response = maintenanceService.updateMaintenance(maintenanceId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteMaintenance(@PathVariable("id") Long maintenanceId)
            throws ResourceNotFoundException {
        maintenanceService.softDeleteMaintenance(maintenanceId);
        return ResponseEntity.ok("Đã xóa lịch bảo trì thành công.");
    }

    @DeleteMapping("/purge/{id}")
    public ResponseEntity<String> hardDeleteMaintenance(@PathVariable("id") Long maintenanceId)
            throws ResourceNotFoundException {
        maintenanceService.hardDeleteMaintenance(maintenanceId);
        return ResponseEntity.ok("Đã xóa lịch bảo trì thành công.");
    }
}
