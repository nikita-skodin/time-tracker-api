package com.skodin.services;

import com.skodin.dtos.StatisticsDto;
import com.skodin.exceprions.BadRequestException;
import com.skodin.exceprions.NotFoundException;
import com.skodin.models.AppUser;
import com.skodin.models.Task;
import com.skodin.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    public Task saveAndFlush(Task entity) {

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

    private List<Task> getTop5TasksByStartTimeNotNullAndStopTimeNull() {
        return taskRepository.getTop5TasksByStartTimeNotNullAndStopTimeNull();
    }

    public void clear() {

        List<Task> list = getTop5TasksByStartTimeNotNullAndStopTimeNull();
        LocalDateTime time = LocalDateTime.now().toLocalDate().atStartOfDay().minusSeconds(1L);

        while (!list.isEmpty()){
            log.info(list);

            // TODO тут оптимизировать
            for (Task task : list) {
                task.setStopTime(time);
                saveAndFlush(task);
            }

            list = getTop5TasksByStartTimeNotNullAndStopTimeNull();
        }
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task startTime(UUID taskId) {

        Task task = findById(taskId);

        if (task.getStartTime() != null) {
            throw new BadRequestException("Task is already start");
        }

        task.setStartTime(LocalDateTime.now());

        return taskRepository.saveAndFlush(task);

    }

    public Task stopTime(UUID taskId) {

        Task task = findById(taskId);

        if (task.getStopTime() != null) {
            throw new BadRequestException("Task is already stop");
        }

        if (task.getStartTime() == null) {
            throw new BadRequestException("Task is not start");
        }

        task.setStopTime(LocalDateTime.now());

        return taskRepository.saveAndFlush(task);

    }

    public void clearUser(AppUser appUser) {
        taskRepository.deleteAllByUser(appUser);
    }
}
