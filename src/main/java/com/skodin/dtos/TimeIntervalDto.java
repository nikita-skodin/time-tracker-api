package com.skodin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TimeIntervalDto {
    private String time;
    private LocalDateTime date;
    private TaskDto task;
}
