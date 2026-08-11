package com.macthien.meetingroombookingsystem.dto;

import com.macthien.meetingroombookingsystem.enums.BookingStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @Size(max = 50, message = "Mã booking không vượt quá 50 ký tự")
    private String bookingCode;

    @NotNull(message = "ID nhân viên đặt không được để trống")
    private Long employeeId;

    @NotNull(message = "ID phòng họp không được để trống")
    private Long roomId;

    @NotNull(message = "Thời gian bắt đầu họp không được để trống")
    @FutureOrPresent(message = "Không thể đặt phòng trong quá khứ")
    private LocalDateTime bookingStartTime;

    @NotNull(message = "Thời gian kết thúc họp không được để trống")
    private LocalDateTime bookingEndTime;

    @NotBlank(message = "Mục đích cuộc họp không được để trống")
    @Size(max = 255, message = "Mục đích họp không vượt quá 255 ký tự")
    private String bookingPurpose;

    @NotNull(message = "Số người tham gia không được để trống")
    @Min(value = 1, message = "Số người tham gia tối thiểu phải là 1")
    private Integer bookingParticipantsCount;

    @NotNull(message = "Trạng thái đặt phòng không được để trống")
    private BookingStatus bookingStatus;

    private String bookingNotes;
}
