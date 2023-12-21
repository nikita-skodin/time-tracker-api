package com.skodin.models;


import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Builder
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_time_tracker")
public class Task {

    @Id
    @GeneratedValue
    UUID id;

    String context;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "stop_time")
    LocalDateTime stopTime;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    AppUser user;

    @Type(PostgreSQLIntervalType.class)
    @Column(columnDefinition = "interval")
    Duration duration;

}

