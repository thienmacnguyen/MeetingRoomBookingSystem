package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.MaintenanceRequest;
import com.macthien.meetingroombookingsystem.dto.MaintenanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaintenanceService {
    MaintenanceResponse createMaintenance(MaintenanceRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    Page<MaintenanceResponse> getAllMaintenances(String search, Pageable pageable);

    MaintenanceResponse getMaintenanceById(Long maintenanceId)
            throws ResourceNotFoundException;

    MaintenanceResponse updateMaintenance(Long maintenanceId, MaintenanceRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    void softDeleteMaintenance(Long maintenanceId)
            throws ResourceNotFoundException;

    void hardDeleteMaintenance(Long maintenanceId)
            throws ResourceNotFoundException;
}
