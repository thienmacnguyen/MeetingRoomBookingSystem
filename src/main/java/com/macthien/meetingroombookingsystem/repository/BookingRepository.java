package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Booking;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
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
            "AND b.bookingStatus <> :cancelledStatus")
    boolean existsFutureBookingByRoomId(
            @Param("roomId") Long roomId,
            @Param("now") LocalDateTime now,
            @Param("cancelledStatus") BookingStatus cancelledStatus
    );
}
