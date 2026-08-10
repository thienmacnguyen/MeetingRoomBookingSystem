package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import com.macthien.meetingroombookingsystem.dto.EmployeeReportResponse;
import com.macthien.meetingroombookingsystem.dto.RoomReportResponse;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import com.macthien.meetingroombookingsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/room/{roomCode}/history")
    public ResponseEntity<Page<BookingResponse>> getRoomBookingHistory(
            @PathVariable("roomCode") String roomCode,
            @RequestParam(value = "employeeName", required = false) String employeeName,
            @RequestParam(value = "purpose", required = false) String purpose,
            @RequestParam(value = "status", required = false) BookingStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "bookingStartTime") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) throws ResourceNotFoundException {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(reportService.getRoomBookingHistory(roomCode, employeeName, purpose, status, pageable));
    }

    @GetMapping("/employee/{employeeCode}/history")
    public ResponseEntity<Page<BookingResponse>> getEmployeeBookingHistory(
            @PathVariable("employeeCode") String employeeCode,
            @RequestParam(value = "roomName", required = false) String roomName,
            @RequestParam(value = "purpose", required = false) String purpose,
            @RequestParam(value = "status", required = false) BookingStatus status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "bookingStartTime") String sortBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) throws ResourceNotFoundException {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(reportService.getEmployeeBookingHistory(employeeCode, roomName, purpose, status, pageable));
    }

    @GetMapping("/bookings-by-room")
    public ResponseEntity<List<RoomReportResponse>> getBookingsByRoom() {
        return ResponseEntity.ok(reportService.getBookingsByRoom());
    }

    @GetMapping("/bookings-by-employee")
    public ResponseEntity<List<EmployeeReportResponse>> getBookingsByEmployee() {
        return ResponseEntity.ok(reportService.getBookingsByEmployee());
    }

    @GetMapping("/most-used-rooms")
    public ResponseEntity<List<RoomReportResponse>> getMostUsedRooms() {
        return ResponseEntity.ok(reportService.getMostUsedRooms());
    }

    @GetMapping("/cancelled-bookings")
    public ResponseEntity<Long> getCancelledBookingsCount() {
        return ResponseEntity.ok(reportService.getCancelledBookingsCount());
    }
}
