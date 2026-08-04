package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.MaintenanceRequest;
import com.macthien.meetingroombookingsystem.dto.MaintenanceResponse;
import com.macthien.meetingroombookingsystem.entity.Maintenance;
import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.enums.MaintenanceStatus;
import com.macthien.meetingroombookingsystem.repository.MaintenanceRepository;
import com.macthien.meetingroombookingsystem.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Override
    public MaintenanceResponse createMaintenance(MaintenanceRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        MeetingRoom room = meetingRoomRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + request.getRoomId()));

        if (request.getMaintenanceStartTime().isAfter(request.getMaintenanceEndTime())) {
            throw new DuplicateCodeException("Thời gian bắt đầu bảo trì phải diễn ra trước thời gian kết thúc.");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setRoom(room);
        maintenance.setMaintenanceStartTime(request.getMaintenanceStartTime());
        maintenance.setMaintenanceEndTime(request.getMaintenanceEndTime());
        maintenance.setMaintenanceReason(request.getMaintenanceReason());
        maintenance.setMaintenanceStatus(request.getMaintenanceStatus());

        Maintenance saved = maintenanceRepository.save(maintenance);
        return mapToResponse(saved);
    }

    @Override
    public Page<MaintenanceResponse> getAllMaintenances(String search, Pageable pageable) {
        Page<Maintenance> list;
        list = maintenanceRepository.searchMaintenances(search, pageable);
        return list.map(this::mapToResponse);
    }

    @Override
    public MaintenanceResponse getMaintenanceById(Long maintenanceId) throws ResourceNotFoundException {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch bảo trì với ID: " + maintenanceId));
        return mapToResponse(maintenance);
    }

    @Override
    public MaintenanceResponse updateMaintenance(Long maintenanceId, MaintenanceRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch bảo trì với ID: " + maintenanceId));

        MeetingRoom room = meetingRoomRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + request.getRoomId()));

        if (request.getMaintenanceStartTime().isAfter(request.getMaintenanceEndTime())) {
            throw new DuplicateCodeException("Thời gian bắt đầu bảo trì phải diễn ra trước thời gian kết thúc.");
        }

        maintenance.setRoom(room);
        maintenance.setMaintenanceStartTime(request.getMaintenanceStartTime());
        maintenance.setMaintenanceEndTime(request.getMaintenanceEndTime());
        maintenance.setMaintenanceReason(request.getMaintenanceReason());
        maintenance.setMaintenanceStatus(request.getMaintenanceStatus());

        Maintenance updated = maintenanceRepository.save(maintenance);
        return mapToResponse(updated);
    }

    @Override
    public void softDeleteMaintenance(Long maintenanceId) throws ResourceNotFoundException {
        Maintenance maintenance = maintenanceRepository.findByMaintenanceIdAndDeletedFalse(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch bảo trì hoạt động với ID: " + maintenanceId));

        maintenance.setDeleted(true);
        maintenance.setMaintenanceStatus(MaintenanceStatus.CANCELLED);
        maintenanceRepository.save(maintenance);
    }

    @Override
    public void hardDeleteMaintenance(Long maintenanceId) throws ResourceNotFoundException {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch bảo trì với ID: " + maintenanceId));

        maintenanceRepository.delete(maintenance);
    }

    private MaintenanceResponse mapToResponse(Maintenance m) {
        MaintenanceResponse response = new MaintenanceResponse();
        response.setMaintenanceId(m.getMaintenanceId());
        response.setMaintenanceStartTime(m.getMaintenanceStartTime());
        response.setMaintenanceEndTime(m.getMaintenanceEndTime());
        response.setMaintenanceReason(m.getMaintenanceReason());
        response.setMaintenanceStatus(m.getMaintenanceStatus());

        if (m.getRoom() != null) {
            response.setRoomId(m.getRoom().getRoomId());
            response.setRoomName(m.getRoom().getRoomName());
        }
        return response;
    }
}
