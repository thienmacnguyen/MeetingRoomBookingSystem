package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Booking;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.room.roomId = :roomId " +
            "AND b.bookingStartTime > :now " +
            "AND b.bookingStatus <> :cancelledStatus ")
    boolean existsFutureBookingByRoomId(
            @Param("roomId") Long roomId,
            @Param("now") LocalDateTime now,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.room.roomId = :roomId " +
            "AND b.bookingStatus <> :cancelledStatus " +
            "AND b.bookingStartTime < :endTime " +
            "AND b.bookingEndTime > :startTime")
    boolean existsOverlapBooking(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.room.roomId = :roomId " +
            "AND b.bookingId <> :bookingId " +
            "AND b.bookingStatus <> :cancelledStatus " +
            "AND b.bookingStartTime < :endTime " +
            "AND b.bookingEndTime > :startTime")
    boolean existsOverlapBookingForUpdate(
            @Param("roomId") Long roomId,
            @Param("bookingId") Long bookingId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );

    @Query("SELECT b FROM Booking b WHERE " +
            "(:search IS NULL OR LOWER(b.room.roomName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.employee.employeeFullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.bookingPurpose) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Booking> searchBookings(@Param("search") String search, Pageable pageable);
}
