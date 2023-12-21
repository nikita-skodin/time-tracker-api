package com.skodin.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    String context;

    @JsonProperty("start_time")
    LocalDateTime startTime;

    @JsonProperty("stop_time")
    LocalDateTime stopTime;

//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonProperty("user_id")
    UUID userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Duration duration;
}
