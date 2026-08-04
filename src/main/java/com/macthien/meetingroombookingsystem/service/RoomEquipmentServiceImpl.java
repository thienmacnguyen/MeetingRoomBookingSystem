package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentResponse;
import com.macthien.meetingroombookingsystem.entity.Equipment;
import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.entity.RoomEquipment;
import com.macthien.meetingroombookingsystem.repository.EquipmentRepository;
import com.macthien.meetingroombookingsystem.repository.MeetingRoomRepository;
import com.macthien.meetingroombookingsystem.repository.RoomEquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomEquipmentServiceImpl implements RoomEquipmentService {
    @Autowired
    private RoomEquipmentRepository roomEquipmentRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public RoomEquipmentResponse assignEquipment(RoomEquipmentRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        MeetingRoom room = meetingRoomRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + request.getRoomId()));

        Equipment equipment = equipmentRepository.findByEquipmentIdAndDeletedFalse(request.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thiết bị hoạt động với ID: " + request.getEquipmentId()));

        if (roomEquipmentRepository.existsByRoomRoomIdAndEquipmentEquipmentId(request.getRoomId(), request.getEquipmentId())) {
            throw new DuplicateCodeException("Thiết bị '" + equipment.getEquipmentName() + "' đã được gán cho phòng họp này trước đó.");
        }

        if (request.getAssignedQuantity() > equipment.getEquipmentTotalQuantity()) {
            throw new DuplicateCodeException("Số lượng gán " + request.getAssignedQuantity() + " vượt quá tổng số lượng hiện có trong kho " + equipment.getEquipmentTotalQuantity() + ".");
        }

        RoomEquipment roomEquipment = new RoomEquipment();
        roomEquipment.setRoom(room);
        roomEquipment.setEquipment(equipment);
        roomEquipment.setAssignedQuantity(request.getAssignedQuantity());

        RoomEquipment saved = roomEquipmentRepository.save(roomEquipment);
        return mapToResponse(saved);
    }

    @Override
    public List<RoomEquipmentResponse> getEquipmentsByRoom(Long roomId) throws ResourceNotFoundException {
        meetingRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + roomId));

        List<RoomEquipment> list = roomEquipmentRepository.findByRoomRoomId(roomId);
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public RoomEquipmentResponse updateAssignedQuantity(Long roomEquipmentId, Integer quantity) throws ResourceNotFoundException, DuplicateCodeException {
        RoomEquipment roomEquipment = roomEquipmentRepository.findById(roomEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin gán thiết bị với ID: " + roomEquipmentId));

        // Kiểm tra số lượng mới so với lượng trong kho
        Equipment equipment = roomEquipment.getEquipment();
        if (quantity > equipment.getEquipmentTotalQuantity()) {
            throw new DuplicateCodeException("Số lượng cập nhật vượt quá tổng số lượng hiện có trong kho (" + equipment.getEquipmentTotalQuantity() + ").");
        }

        roomEquipment.setAssignedQuantity(quantity);
        RoomEquipment updated = roomEquipmentRepository.save(roomEquipment);
        return mapToResponse(updated);
    }

    @Override
    public void unassignEquipment(Long roomEquipmentId) throws ResourceNotFoundException {
        RoomEquipment roomEquipment = roomEquipmentRepository.findById(roomEquipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thông tin gán thiết bị với ID: " + roomEquipmentId));

        // Đối với bảng trung gian liên kết, chúng ta xóa cứng dòng liên kết này đi (hủy gán)
        roomEquipmentRepository.delete(roomEquipment);
        System.err.println("Gỡ thiết bị khỏi phòng họp thành công");
    }

    private RoomEquipmentResponse mapToResponse(RoomEquipment roomEquipment) {
        RoomEquipmentResponse response = new RoomEquipmentResponse();
        response.setRoomEquipmentId(roomEquipment.getRoomEquipmentId());

        if (roomEquipment.getRoom() != null) {
            response.setRoomId(roomEquipment.getRoom().getRoomId());
            response.setRoomName(roomEquipment.getRoom().getRoomName());
        }

        if (roomEquipment.getEquipment() != null) {
            response.setEquipmentId(roomEquipment.getEquipment().getEquipmentId());
            response.setEquipmentName(roomEquipment.getEquipment().getEquipmentName());
        }

        response.setAssignedQuantity(roomEquipment.getAssignedQuantity());
        return response;
    }
}
