package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long roomId;
    private String roomCode;
    private String roomName;
    private Integer roomFloor;
    private Integer roomCapacity;
    private RoomStatus roomStatus;
    private String roomDescription;
}
