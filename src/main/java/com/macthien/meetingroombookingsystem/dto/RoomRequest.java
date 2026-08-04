package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.RoomStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomRequest {
    @Size(max = 50, message = "Mã phòng không được vượt quá 50 ký tự")
    private String roomCode;

    @NotBlank(message = "Tên phòng không được để trống")
    @Size(max = 100, message = "Tên phòng không được vượt quá 100 ký tự")
    private String roomName;

    @NotNull(message = "Số tầng không được để trống")
    @Min(value = 0, message = "Số tầng phải là số không âm")
    private Integer roomFloor;

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa tối thiểu phải là 1 người")
    private Integer roomCapacity;


    private RoomStatus roomStatus;

    private String roomDescription;
}
