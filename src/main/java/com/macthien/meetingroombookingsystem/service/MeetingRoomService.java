package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.RoomRequest;
import com.macthien.meetingroombookingsystem.dto.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetingRoomService {
    RoomResponse createRoom(RoomRequest request)
            throws DuplicateCodeException;

    Page<RoomResponse> getAllRooms(String search, Pageable pageable);
    RoomResponse getRoomById(Long roomId)
            throws ResourceNotFoundException;
    RoomResponse updateRoom(Long roomId, RoomRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;
    void softDeleteRoom(Long roomId)
            throws ResourceNotFoundException, DeleteConstraintException;
    void hardDeleteRoom(Long roomId)
            throws ResourceNotFoundException, DeleteConstraintException;
}
