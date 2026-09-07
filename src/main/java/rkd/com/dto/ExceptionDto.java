package rkd.com.dto;

import rkd.com.type.ExceptionType;

import java.time.LocalDateTime;

public class ExceptionDto {

    private String message;
    private ExceptionType type;
    private LocalDateTime timestamp;

    public ExceptionDto(String message, ExceptionType type, LocalDateTime timestamp) {
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
