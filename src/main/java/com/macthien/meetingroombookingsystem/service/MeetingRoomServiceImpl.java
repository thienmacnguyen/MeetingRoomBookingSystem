package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import com.macthien.meetingroombookingsystem.dto.RoomRequest;
import com.macthien.meetingroombookingsystem.dto.RoomResponse;
import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import com.macthien.meetingroombookingsystem.repository.MeetingRoomRepository;
import com.macthien.meetingroombookingsystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {
    @Autowired
    private MeetingRoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public RoomResponse createRoom(RoomRequest request) throws DuplicateCodeException {
        String code = request.getRoomCode();

        if (code == null || code.trim().isEmpty()) {
            code = generateNextRoomCode();
        } else {
            if (roomRepository.existsByRoomCode(code)) {
                throw new DuplicateCodeException("Mã phòng họp '" + code + "' đã tồn tại.");
            }
        }

        MeetingRoom room = new MeetingRoom();
        room.setRoomCode(code);
        room.setRoomName(request.getRoomName());
        room.setRoomFloor(request.getRoomFloor());
        room.setRoomCapacity(request.getRoomCapacity());
        room.setRoomStatus(RoomStatus.ACTIVE);
        room.setRoomDescription(request.getRoomDescription());

        MeetingRoom saved = roomRepository.save(room);
        return mapToResponse(saved);
    }

    @Override
    public Page<RoomResponse> getAllRooms(String search, Pageable pageable) {
        Page<MeetingRoom> rooms;
        rooms = roomRepository.searchRooms(search, RoomStatus.OUT_OF_SERVICE, pageable);
        return rooms.map(this::mapToResponse);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) throws ResourceNotFoundException {
        MeetingRoom room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + roomId));
        return mapToResponse(room);
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        MeetingRoom room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + roomId));

        if (roomRepository.existsByRoomCodeAndRoomIdNot(request.getRoomCode(), roomId)) {
            throw new DuplicateCodeException("Mã phòng họp '" + request.getRoomCode() + "' đã được sử dụng bởi phòng khác.");
        }

        room.setRoomCode(request.getRoomCode());
        room.setRoomName(request.getRoomName());
        room.setRoomFloor(request.getRoomFloor());
        room.setRoomCapacity(request.getRoomCapacity());
        room.setRoomStatus(request.getRoomStatus());
        room.setRoomDescription(request.getRoomDescription());

        MeetingRoom updated = roomRepository.save(room);
        return mapToResponse(updated);
    }

    @Override
    public void softDeleteRoom(Long roomId) throws ResourceNotFoundException, DeleteConstraintException {
        MeetingRoom room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + roomId));

        LocalDateTime now = LocalDateTime.now();
        if (bookingRepository.existsFutureBookingByRoomId(roomId, now, BookingStatus.CANCELLED)) {
            throw new DeleteConstraintException("Không thể xóa phòng họp này vì vẫn còn các lịch đặt phòng hoạt động trong tương lai.");
        }

        room.setRoomStatus(RoomStatus.OUT_OF_SERVICE);
        roomRepository.save(room);

        System.err.println("Đã xóa phòng họp: " + room.getRoomName());


    }
    @Override
    public void hardDeleteRoom(Long roomId) throws ResourceNotFoundException, DeleteConstraintException {
        MeetingRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp với ID: " + roomId));

        LocalDateTime now = LocalDateTime.now();
        if (bookingRepository.existsFutureBookingByRoomId(roomId, now, BookingStatus.CANCELLED)) {
            throw new DeleteConstraintException("Không thể xóa phòng họp này vì vẫn còn các lịch đặt phòng hoạt động trong tương lai.");
        }

        roomRepository.delete(room);
        System.err.println("Đã xóa phòng họp: " + room.getRoomName());
    }

    private String generateNextRoomCode() {
        String prefix = "H";
        int defaultStartNumber = 201;

        Optional<MeetingRoom> lastRoomOpt = roomRepository
                .findFirstByRoomCodeStartingWithOrderByRoomCodeDesc(prefix);

        if (lastRoomOpt.isEmpty()) {
            return prefix + defaultStartNumber;
        }

        String lastCode = lastRoomOpt.get().getRoomCode();
        try {
            String numericPart = lastCode.substring(prefix.length());
            int lastNumber = Integer.parseInt(numericPart);
            int nextNumber = lastNumber + 1;

            return prefix + nextNumber;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return prefix + defaultStartNumber;
        }
    }

    private RoomResponse mapToResponse(MeetingRoom room) {
        RoomResponse response = new RoomResponse();
        response.setRoomId(room.getRoomId());
        response.setRoomCode(room.getRoomCode());
        response.setRoomName(room.getRoomName());
        response.setRoomFloor(room.getRoomFloor());
        response.setRoomCapacity(room.getRoomCapacity());
        response.setRoomStatus(room.getRoomStatus());
        response.setRoomDescription(room.getRoomDescription());
        return response;
    }
}
