package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentRequest;
import com.macthien.meetingroombookingsystem.dto.RoomEquipmentResponse;
import com.macthien.meetingroombookingsystem.service.RoomEquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-equipments")
public class RoomEquipmentController {
    @Autowired
    private RoomEquipmentService roomEquipmentService;

    @PostMapping("/assign")
    public ResponseEntity<RoomEquipmentResponse> assignEquipment(@Valid @RequestBody RoomEquipmentRequest request)
            throws ResourceNotFoundException, DuplicateCodeException {
        RoomEquipmentResponse response = roomEquipmentService.assignEquipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomEquipmentResponse>> getEquipmentsByRoom(@PathVariable("roomId") Long roomId)
            throws ResourceNotFoundException {
        List<RoomEquipmentResponse> list = roomEquipmentService.getEquipmentsByRoom(roomId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RoomEquipmentResponse> updateAssignedQuantity(
            @PathVariable("id") Long roomEquipmentId,
            @RequestParam("quantity") Integer quantity
    ) throws ResourceNotFoundException, DuplicateCodeException {
        RoomEquipmentResponse response = roomEquipmentService.updateAssignedQuantity(roomEquipmentId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> unassignEquipment(@PathVariable("id") Long roomEquipmentId) throws ResourceNotFoundException {
        roomEquipmentService.unassignEquipment(roomEquipmentId);
        return ResponseEntity.ok("Đã gỡ thiết bị khỏi phòng họp thành công");
    }

}
