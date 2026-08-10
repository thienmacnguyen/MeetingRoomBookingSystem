package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.Booking;
import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.room.roomCode = :roomCode " +
            "AND (:employeeName IS NULL OR :employeeName = '' OR LOWER(b.employee.employeeFullName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
            "AND (:purpose IS NULL OR :purpose = '' OR LOWER(b.bookingPurpose) LIKE LOWER(CONCAT('%', :purpose, '%'))) " +
            "AND (:status IS NULL OR b.bookingStatus = :status)")
    Page<Booking> findRoomHistory(
            @Param("roomCode") String roomCode,
            @Param("employeeName") String employeeName,
            @Param("purpose") String purpose,
            @Param("status") BookingStatus status,
            Pageable pageable
    );

    @Query("SELECT b FROM Booking b WHERE b.employee.employeeCode = :employeeCode " +
            "AND (:roomName IS NULL OR :roomName = '' OR LOWER(b.room.roomName) LIKE LOWER(CONCAT('%', :roomName, '%'))) " +
            "AND (:purpose IS NULL OR :purpose = '' OR LOWER(b.bookingPurpose) LIKE LOWER(CONCAT('%', :purpose, '%'))) " +
            "AND (:status IS NULL OR b.bookingStatus = :status)")
    Page<Booking> findEmployeeHistory(
            @Param("employeeCode") String employeeCode,
            @Param("roomName") String roomName,
            @Param("purpose") String purpose,
            @Param("status") BookingStatus status,
            Pageable pageable
    );

    @Query("SELECT b.room.roomId, b.room.roomName, COUNT(b) FROM Booking b GROUP BY b.room.roomId, b.room.roomName")
    List<Object[]> countBookingsByRoom();

    @Query("SELECT b.employee.employeeId, b.employee.employeeFullName, COUNT(b) FROM Booking b GROUP BY b.employee.employeeId, b.employee.employeeFullName")
    List<Object[]> countBookingsByEmployee();

    @Query("SELECT b.room.roomId, b.room.roomName, COUNT(b) as bookingCount " +
            "FROM Booking b GROUP BY b.room.roomId, b.room.roomName ORDER BY bookingCount DESC")
    List<Object[]> findMostUsedRooms();

    long countByBookingStatus(BookingStatus status);
}
