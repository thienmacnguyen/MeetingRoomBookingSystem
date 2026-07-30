package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.EquipmentResponse;
import com.macthien.meetingroombookingsystem.service.EquipmentService;
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
@RequestMapping("/api/equipments")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping("/create")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody EquipmentRequest request)
            throws DuplicateCodeException {
        EquipmentResponse response = equipmentService.createEquipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable("id") Long equipmentId)
            throws ResourceNotFoundException {
        EquipmentResponse response = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EquipmentResponse>> getAllEquipments(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "equipmentId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EquipmentResponse> response = equipmentService.getAllEquipments(search, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable("id") Long equipmentId,
            @Valid @RequestBody EquipmentRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        EquipmentResponse response = equipmentService.updateEquipment(equipmentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteEquipment(@PathVariable("id") Long equipmentId)
            throws ResourceNotFoundException {
        equipmentService.softDeleteEquipment(equipmentId);
        return ResponseEntity.ok("Đã xóa thiết bị thành công.");
    }

    @DeleteMapping("/purge/{id}")
    public ResponseEntity<String> hardDeleteEquipment(@PathVariable("id") Long equipmentId) throws ResourceNotFoundException {
        equipmentService.hardDeleteEquipment(equipmentId);
        return ResponseEntity.ok("Đã xóa thiết bị thành công.");
        }
}
