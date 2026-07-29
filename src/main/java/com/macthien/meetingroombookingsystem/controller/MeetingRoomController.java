package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.RoomRequest;
import com.macthien.meetingroombookingsystem.dto.RoomResponse;
import com.macthien.meetingroombookingsystem.service.MeetingRoomService;
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
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {
    @Autowired
    private MeetingRoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request)
            throws DuplicateCodeException {
        RoomResponse response = roomService.createRoom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("id") Long roomId)
            throws ResourceNotFoundException {
        RoomResponse response = roomService.getRoomById(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getAllRooms(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "roomId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RoomResponse> response = roomService.getAllRooms(search, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable("id") Long roomId,
            @Valid @RequestBody RoomRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        RoomResponse response = roomService.updateRoom(roomId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> softDeleteRoom(@PathVariable("id") Long roomId)
            throws ResourceNotFoundException, DeleteConstraintException {
        roomService.softDeleteRoom(roomId);
        return ResponseEntity.ok("Đã xóa phòng họp thành công.");
    }

    @DeleteMapping("/purge/{id}")
    public ResponseEntity<String> hardDeleteRoom(@PathVariable("id") Long roomId)
            throws ResourceNotFoundException, DeleteConstraintException {
        roomService.hardDeleteRoom(roomId);
        return ResponseEntity.ok("Đã xóa phòng họp thành công.");
    }
}
