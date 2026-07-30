package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentResponse;

import java.util.List;

public interface RoomEquipmentService {
    RoomEquipmentResponse assignEquipment(RoomEquipmentRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    List<RoomEquipmentResponse> getEquipmentsByRoom(Long roomId)
            throws ResourceNotFoundException;

    RoomEquipmentResponse updateAssignedQuantity(Long roomEquipmentId, Integer quantity)
            throws ResourceNotFoundException, DuplicateCodeException;

    void unassignEquipment(Long roomEquipmentId)
            throws ResourceNotFoundException;
}
