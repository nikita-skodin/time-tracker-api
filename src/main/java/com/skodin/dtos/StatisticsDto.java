package com.skodin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatisticsDto {
    private Long days;
    private Long hours;
    private Long minutes;
}
