package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import com.macthien.meetingroombookingsystem.dto.EmployeeReportResponse;
import com.macthien.meetingroombookingsystem.dto.RoomReportResponse;
import com.macthien.meetingroombookingsystem.entity.Booking;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import com.macthien.meetingroombookingsystem.repository.EmployeeRepository;
import com.macthien.meetingroombookingsystem.repository.MeetingRoomRepository;
import com.macthien.meetingroombookingsystem.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MeetingRoomRepository roomRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Page<BookingResponse> getRoomBookingHistory(String roomCode, String employeeName, String purpose, BookingStatus status, Pageable pageable) throws ResourceNotFoundException {
        roomRepository.findByRoomCodeAndRoomStatusNot(roomCode, RoomStatus.OUT_OF_SERVICE)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với mã: " + roomCode));

        Page<Booking> history = reportRepository.findRoomHistory(roomCode, employeeName, purpose, status, pageable);
        return history.map(this::mapToBookingResponse);
    }

    @Override
    public Page<BookingResponse> getEmployeeBookingHistory(String employeeCode, String roomName, String purpose, BookingStatus status, Pageable pageable) throws ResourceNotFoundException {
        employeeRepository.findByEmployeeCodeAndEmployeeStatus(employeeCode, EmployeeStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên hoạt động với mã: " + employeeCode));

        Page<Booking> history = reportRepository.findEmployeeHistory(employeeCode, roomName, purpose, status, pageable);
        return history.map(this::mapToBookingResponse);
    }

    @Override
    public List<RoomReportResponse> getBookingsByRoom() {

        List<Object[]> results = reportRepository.countBookingsByRoom();
        return results.stream()
                .map(obj -> new RoomReportResponse((Long) obj[0], (String) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeReportResponse> getBookingsByEmployee() {

        List<Object[]> results = reportRepository.countBookingsByEmployee();
        return results.stream()
                .map(obj -> new EmployeeReportResponse((Long) obj[0], (String) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomReportResponse> getMostUsedRooms() {

        List<Object[]> results = reportRepository.findMostUsedRooms();
        return results.stream()
                .map(obj -> new RoomReportResponse((Long) obj[0], (String) obj[1], (Long) obj[2]))
                .collect(Collectors.toList());
    }

    @Override
    public long getCancelledBookingsCount() {

        return reportRepository.countByBookingStatus(BookingStatus.CANCELLED);
    }

    private BookingResponse mapToBookingResponse(Booking b) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(b.getBookingId());
        response.setBookingCode(b.getBookingCode());
        response.setBookingStartTime(b.getBookingStartTime());
        response.setBookingEndTime(b.getBookingEndTime());
        response.setBookingPurpose(b.getBookingPurpose());
        response.setBookingParticipantsCount(b.getBookingParticipantsCount());
        response.setBookingStatus(b.getBookingStatus());
        response.setBookingNotes(b.getBookingNotes());

        if (b.getEmployee() != null) {
            response.setEmployeeId(b.getEmployee().getEmployeeId());
            response.setEmployeeFullName(b.getEmployee().getEmployeeFullName());
        }

        if (b.getRoom() != null) {
            response.setRoomId(b.getRoom().getRoomId());
            response.setRoomName(b.getRoom().getRoomName());
        }
        return response;
    }
}
