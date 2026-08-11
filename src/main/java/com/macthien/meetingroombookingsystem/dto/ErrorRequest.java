package com.macthien.meetingroombookingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRequest {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
