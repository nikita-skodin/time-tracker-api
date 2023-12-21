package com.skodin.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
public final class TimePeriodDto {
    @JsonProperty("start_time")
    private LocalDateTime startTime;
    @JsonProperty("stop_time")
    private LocalDateTime stopTime;
}
