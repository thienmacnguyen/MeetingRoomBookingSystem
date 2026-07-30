package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipmentService {
    EquipmentResponse createEquipment(EquipmentRequest request)
            throws DuplicateCodeException;

    Page<EquipmentResponse> getAllEquipments(String search, Pageable pageable);

    EquipmentResponse getEquipmentById(Long equipmentId)
            throws ResourceNotFoundException;

    EquipmentResponse updateEquipment(Long equipmentId, EquipmentRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    // API Xóa mềm thiết bị
    void softDeleteEquipment(Long equipmentId)
            throws ResourceNotFoundException;

    // API Xóa cứng thiết bị
    void hardDeleteEquipment(Long equipmentId)
            throws ResourceNotFoundException;
}
