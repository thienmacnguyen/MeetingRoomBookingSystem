package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import com.macthien.meetingroombookingsystem.dto.EmployeeReportResponse;
import com.macthien.meetingroombookingsystem.dto.RoomReportResponse;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    Page<BookingResponse> getRoomBookingHistory(
            String roomCode, String employeeName, String purpose, BookingStatus status, Pageable pageable)
            throws ResourceNotFoundException;

    Page<BookingResponse> getEmployeeBookingHistory(
            String employeeCode, String roomName, String purpose, BookingStatus status, Pageable pageable)
            throws ResourceNotFoundException;

    List<RoomReportResponse> getBookingsByRoom();

    List<EmployeeReportResponse> getBookingsByEmployee();

    List<RoomReportResponse> getMostUsedRooms();

    long getCancelledBookingsCount();
}
