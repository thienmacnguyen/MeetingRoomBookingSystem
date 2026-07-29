package com.macthien.meetingroombookingsystem.repository;

import com.macthien.meetingroombookingsystem.entity.MeetingRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    Optional<MeetingRoom> findByRoomIdAndDeletedFalse(Long roomId);
    boolean existsByRoomCodeAndDeletedFalse(String roomCode);
    boolean existsByRoomCodeAndRoomIdNotAndDeletedFalse(String roomCode, Long roomId);
    Optional<MeetingRoom> findFirstByRoomCodeStartingWithAndDeletedFalseOrderByRoomCodeDesc(String prefix);
    Page<MeetingRoom> findAllByDeletedFalse(Pageable pageable);
    @Query("SELECT r FROM MeetingRoom r WHERE r.deleted = false AND LOWER(r.roomName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MeetingRoom> searchActiveRooms(@Param("search") String search, Pageable pageable);

}
