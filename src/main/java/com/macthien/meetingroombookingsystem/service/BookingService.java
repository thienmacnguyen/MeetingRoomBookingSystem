package com.macthien.meetingroombookingsystem.service;

import com.macthien.meetingroombookingsystem.Exception.DuplicateCodeException;
import com.macthien.meetingroombookingsystem.Exception.ResourceNotFoundException;
import com.macthien.meetingroombookingsystem.dto.BookingRequest;
import com.macthien.meetingroombookingsystem.dto.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    Page<BookingResponse> getAllBookings(String search, Pageable pageable);

    BookingResponse getBookingById(Long bookingId)
            throws ResourceNotFoundException;

    BookingResponse updateBooking(Long bookingId, BookingRequest request)
            throws ResourceNotFoundException, DuplicateCodeException;

    void cancelBooking(Long bookingId)
            throws ResourceNotFoundException, DuplicateCodeException;

    void hardDeleteBooking(Long bookingId)
            throws ResourceNotFoundException, DuplicateCodeException;
}
