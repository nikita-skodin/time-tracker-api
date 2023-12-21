package com.skodin.services;

import com.skodin.exceprions.BadRequestException;
import com.skodin.exceprions.NotFoundException;
import com.skodin.models.Task;
import com.skodin.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public <S extends Task> S saveAndFlush(S entity) {

        List<Task> tasks = entity.getUser().getTasks();
        tasks.add(entity);

        return taskRepository.saveAndFlush(entity);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByUserAndTime(UUID userId, Instant startTime, Instant stopTime) {
        return taskRepository.getTasksByUserAndTime(userId, startTime, stopTime);
    }

    @Transactional(readOnly = true)
    public Task findById(UUID uuid) {
        return taskRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Task is not found"));
    }

    public Task startTime(UUID taskId) {

        Task task = findById(taskId);

        if (task.getStartTime() != null){
            throw new BadRequestException("Task is already start");
        }

        task.setStartTime(LocalDateTime.now());

        return taskRepository.saveAndFlush(task);

    }

    public Task stopTime(UUID taskId) {

        Task task = findById(taskId);

        if (task.getStopTime() != null){
            throw new BadRequestException("Task is already stop");
        }

        if (task.getStartTime() == null){
            throw new BadRequestException("Task is not start");
        }

        task.setStopTime(LocalDateTime.now());

        return taskRepository.saveAndFlush(task);

    }
}
