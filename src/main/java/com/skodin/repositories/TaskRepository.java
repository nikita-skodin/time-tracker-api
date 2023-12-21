package com.skodin.repositories;

import com.skodin.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query(value = "SELECT * FROM public.task_time_tracker " +
            "WHERE user_id = :user_id AND start_time >= :start_time AND stop_time <= :stop_time",
            nativeQuery = true)
    List<Task> getTasksByUserAndTime(@Param("user_id") UUID userId,
                                            @Param("start_time") Instant startTime,
                                            @Param("stop_time") Instant stopTime);

}
