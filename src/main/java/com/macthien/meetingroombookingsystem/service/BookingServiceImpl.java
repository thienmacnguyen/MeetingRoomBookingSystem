package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingRequest;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import com.macthien.meetingroombookingsystem.entity.Booking;
import com.macthien.meetingroombookingsystem.entity.Employee;
import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import com.macthien.meetingroombookingsystem.enums.EmployeeStatus;
import com.macthien.meetingroombookingsystem.enums.MaintenanceStatus;
import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import com.macthien.meetingroombookingsystem.repository.BookingRepository;
import com.macthien.meetingroombookingsystem.repository.EmployeeRepository;
import com.macthien.meetingroombookingsystem.repository.MaintenanceRepository;
import com.macthien.meetingroombookingsystem.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MeetingRoomRepository roomRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + request.getEmployeeId()));

        if (employee.getEmployeeStatus() == EmployeeStatus.INACTIVE) {
            throw new ResourceNotFoundException("Nhân viên hiện đang ở trạng thái ngừng hoạt động, không thể thực hiện đặt phòng.");
        }

        MeetingRoom room = roomRepository.findByRoomIdAndRoomStatusNot(request.getRoomId(), RoomStatus.OUT_OF_SERVICE)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + request.getRoomId()));

        if (request.getBookingStartTime().isAfter(request.getBookingEndTime()) || request.getBookingStartTime().isEqual(request.getBookingEndTime())) {
            throw new DuplicateCodeException("Thời gian bắt đầu họp phải diễn ra trước thời gian kết thúc.");
        }

        if (request.getBookingStartTime().isBefore(LocalDateTime.now())) {
            throw new DuplicateCodeException("Không được phép đặt phòng họp trong quá khứ.");
        }

        if (request.getBookingParticipantsCount() > room.getRoomCapacity()) {
            throw new DuplicateCodeException("Số người tham gia cuộc họp vượt quá sức chứa tối đa của phòng họp (" + room.getRoomCapacity() + " người).");
        }

        if (room.getRoomStatus() != RoomStatus.ACTIVE) {
            throw new DuplicateCodeException("Phòng họp hiện đang bận (Trạng thái: " + room.getRoomStatus() + ").");
        }

        if (maintenanceRepository.existsOverlapMaintenance(request.getRoomId(), request.getBookingStartTime(), request.getBookingEndTime(), request.getBookingStatus())) {
            throw new DuplicateCodeException("Phòng họp đang có lịch bảo trì trùng với thời gian họp yêu cầu.");
        }

        if (bookingRepository.existsOverlapBooking(request.getRoomId(), request.getBookingStartTime(), request.getBookingEndTime(), BookingStatus.CANCELLED)) {
            throw new DuplicateCodeException("Phòng họp đã được đặt bởi cuộc họp khác trong khoảng thời gian này.");
        }

        String code = request.getBookingCode();

        if (code == null || code.trim().isEmpty()) {
            code = generateBookingCode();
        }

        Booking booking = new Booking();
        booking.setBookingCode(code);
        booking.setEmployee(employee);
        booking.setRoom(room);
        booking.setBookingStartTime(request.getBookingStartTime());
        booking.setBookingEndTime(request.getBookingEndTime());
        booking.setBookingPurpose(request.getBookingPurpose());
        booking.setBookingParticipantsCount(request.getBookingParticipantsCount());
        booking.setBookingStatus(request.getBookingStatus());
        booking.setBookingNotes(request.getBookingNotes());

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    public Page<BookingResponse> getAllBookings(String search, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.searchBookings(search, pageable);
        return bookings.map(this::mapToResponse);
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt phòng với ID: " + bookingId));
        return mapToResponse(booking);
    }

    @Override
    public BookingResponse updateBooking(Long bookingId, BookingRequest request) throws ResourceNotFoundException, DuplicateCodeException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt phòng với ID: " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED || booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new DuplicateCodeException("Không được phép chỉnh sửa lịch đặt phòng đã hoàn thành hoặc đã hủy.");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + request.getEmployeeId()));
        if (employee.getEmployeeStatus() == EmployeeStatus.INACTIVE) {
            throw new ResourceNotFoundException("Nhân viên gán lịch đang ở trạng thái ngừng hoạt động.");
        }

        MeetingRoom room = roomRepository.findByRoomIdAndRoomStatusNot(request.getRoomId(), RoomStatus.OUT_OF_SERVICE)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng họp hoạt động với ID: " + request.getRoomId()));

        if (request.getBookingStartTime().isAfter(request.getBookingEndTime()) || request.getBookingStartTime().isEqual(request.getBookingEndTime())) {
            throw new DuplicateCodeException("Thời gian bắt đầu họp phải diễn ra trước thời gian kết thúc.");
        }

        if (request.getBookingParticipantsCount() > room.getRoomCapacity()) {
            throw new DuplicateCodeException("Số lượng người vượt quá sức chứa tối đa của phòng họp (" + room.getRoomCapacity() + " người).");
        }

        if (room.getRoomStatus() != RoomStatus.ACTIVE) {
            throw new DuplicateCodeException("Phòng họp hiện tại không hoạt động.");
        }

        if (maintenanceRepository.existsOverlapMaintenance(request.getRoomId(), request.getBookingStartTime(), request.getBookingEndTime(), request.getBookingStatus())) {
            throw new DuplicateCodeException("Phòng họp đang bận do lịch bảo trì trùng với thời gian họp.");
        }

        if (bookingRepository.existsOverlapBookingForUpdate(request.getRoomId(), bookingId, request.getBookingStartTime(), request.getBookingEndTime(), BookingStatus.CANCELLED)) {
            throw new DuplicateCodeException("Phòng họp đã được đặt bởi cuộc họp khác trong khoảng thời gian này.");
        }

        booking.setEmployee(employee);
        booking.setRoom(room);
        booking.setBookingStartTime(request.getBookingStartTime());
        booking.setBookingEndTime(request.getBookingEndTime());
        booking.setBookingPurpose(request.getBookingPurpose());
        booking.setBookingParticipantsCount(request.getBookingParticipantsCount());
        booking.setBookingStatus(request.getBookingStatus());
        booking.setBookingNotes(request.getBookingNotes());

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }


    @Override
    public void cancelBooking(Long bookingId) throws ResourceNotFoundException, DuplicateCodeException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt phòng với ID: " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new DuplicateCodeException("Không thể thực hiện hủy một lịch đặt phòng đã hoàn tất.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public void hardDeleteBooking(Long bookingId) throws ResourceNotFoundException, DuplicateCodeException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lịch đặt phòng với ID: " + bookingId));

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new DuplicateCodeException("Không thể thực hiện hủy một lịch đặt phòng đã hoàn tất.");
        }

        bookingRepository.delete(booking);
    }

    private String generateBookingCode() {
        Optional<Booking> lastBooking =
                bookingRepository.findFirstByBookingCodeStartingWithOrderByBookingCodeDesc("BK");

        if (lastBooking.isEmpty()) {
            return "BK001";
        }

        String lastCode = lastBooking.get().getBookingCode();
        int number = Integer.parseInt(lastCode.substring(2));

        return String.format("BK%03d", number + 1);


    }

    private BookingResponse mapToResponse(Booking b) {
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
