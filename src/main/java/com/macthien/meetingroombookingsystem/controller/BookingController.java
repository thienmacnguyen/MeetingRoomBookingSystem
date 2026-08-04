package com.macthien.meetingroombookingsystem.controller;

import com.macthien.meetingroombookingsystem.Exception.DeleteConstraintException;
import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingRequest;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import com.macthien.meetingroombookingsystem.service.BookingService;
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
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request)
            throws ResourceNotFoundException, DuplicateCodeException {
        BookingResponse response = bookingService.createBooking(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable("id") Long bookingId)
            throws ResourceNotFoundException {
        BookingResponse response = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAllBookings(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "bookingId") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookingResponse> response = bookingService.getAllBookings(search, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable("id") Long bookingId,
            @Valid @RequestBody BookingRequest request
    ) throws ResourceNotFoundException, DuplicateCodeException {
        BookingResponse response = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable("id") Long bookingId)
            throws ResourceNotFoundException, DuplicateCodeException {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Hủy lịch đặt phòng họp thành công.");
    }

    @DeleteMapping("/purge/{id}")
    public ResponseEntity<String> hardDeleteBooking(
            @PathVariable("id") Long bookingId
    ) throws ResourceNotFoundException, DuplicateCodeException {
        bookingService.hardDeleteBooking(bookingId);
        return ResponseEntity.ok("Xóa lịch đặt phòng thành công");
    }
}
