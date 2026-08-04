package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.EquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.EquipmentResponse;
import com.macthien.meetingroombookingsystem.entity.Equipment;
import com.macthien.meetingroombookingsystem.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public EquipmentResponse createEquipment(EquipmentRequest request) throws DuplicateCodeException {
        if (equipmentRepository.existsByEquipmentCodeAndDeletedFalse(request.getEquipmentCode())) {
            throw new DuplicateCodeException("Mã thiết bị '" + request.getEquipmentCode() + "' đã tồn tại.");
        }

        Equipment equipment = new Equipment();
        equipment.setEquipmentCode(request.getEquipmentCode());
        equipment.setEquipmentName(request.getEquipmentName());
        equipment.setEquipmentType(request.getEquipmentType());
        equipment.setEquipmentTotalQuantity(request.getEquipmentTotalQuantity());
        equipment.setEquipmentCondition(request.getEquipmentCondition());
        equipment.setDeleted(false);

        Equipment saved = equipmentRepository.save(equipment);
        return mapToResponse(saved);
    }

    @Override
    public Page<EquipmentResponse> getAllEquipments(String search, Pageable pageable) {
        Page<Equipment> equipments;
        equipments = equipmentRepository.searchEquipments(search, pageable);
        return equipments.map(this::mapToResponse);
    }

    @Override
    public EquipmentResponse getEquipmentById(Long equipmentId) throws ResourceNotFoundException {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndDeletedFalse(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị hoạt động với ID: " + equipmentId));
        return mapToResponse(equipment);
    }

    @Override
    public EquipmentResponse updateEquipment(Long equipmentId, EquipmentRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndDeletedFalse(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị hoạt động với ID: " + equipmentId));

        if (equipmentRepository.existsByEquipmentCodeAndEquipmentIdNotAndDeletedFalse(request.getEquipmentCode(), equipmentId)) {
            throw new DuplicateCodeException("Mã thiết bị '" + request.getEquipmentCode() + "' đã được sử dụng.");
        }

        if (equipmentRepository.existsByEquipmentCodeAndEquipmentIdNotAndDeletedFalse(request.getEquipmentCode(), equipmentId)) {
            throw new DuplicateCodeException("Mã thiết bị '" + request.getEquipmentCode() + "' đã được sử dụng.");
        }

        equipment.setEquipmentCode(request.getEquipmentCode());
        equipment.setEquipmentName(request.getEquipmentName());
        equipment.setEquipmentType(request.getEquipmentType());
        equipment.setEquipmentTotalQuantity(request.getEquipmentTotalQuantity());
        equipment.setEquipmentCondition(request.getEquipmentCondition());

        Equipment updated = equipmentRepository.save(equipment);
        return mapToResponse(updated);
    }

    @Override
    public void softDeleteEquipment(Long equipmentId) throws ResourceNotFoundException {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndDeletedFalse(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị hoạt động với ID: " + equipmentId));

        equipment.setDeleted(true);
        equipmentRepository.save(equipment);
    }

    @Override
    public void hardDeleteEquipment(Long equipmentId) throws ResourceNotFoundException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị với ID: " + equipmentId));

        equipmentRepository.delete(equipment);

    }

    private EquipmentResponse mapToResponse(Equipment equipment) {
        EquipmentResponse response = new EquipmentResponse();
        response.setEquipmentId(equipment.getEquipmentId());
        response.setEquipmentCode(equipment.getEquipmentCode());
        response.setEquipmentName(equipment.getEquipmentName());
        response.setEquipmentType(equipment.getEquipmentType());
        response.setEquipmentTotalQuantity(equipment.getEquipmentTotalQuantity());
        response.setEquipmentCondition(equipment.getEquipmentCondition());
        return response;
    }
}
