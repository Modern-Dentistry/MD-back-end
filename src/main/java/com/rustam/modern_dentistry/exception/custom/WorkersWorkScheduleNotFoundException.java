package com.rustam.modern_dentistry.exception.custom;

import lombok.Getter;

@Getter
public class WorkersWorkScheduleNotFoundException extends RuntimeException{
    private final String message;
    public WorkersWorkScheduleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
