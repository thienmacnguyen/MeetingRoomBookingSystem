package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    Optional<MeetingRoom> findByRoomId(Long roomId);
    boolean existsByRoomCode(String roomCode);
    boolean existsByRoomCodeAndRoomIdNot(String roomCode, Long roomId);
    Optional<MeetingRoom> findFirstByRoomCodeStartingWithOrderByRoomCodeDesc(String prefix);
    @Query("SELECT r FROM MeetingRoom r WHERE r.roomStatus <> :status AND LOWER(r.roomName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MeetingRoom> searchRooms(@Param("search") String search, @Param("status") RoomStatus status, Pageable pageable);

}
