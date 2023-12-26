package com.skodin.services;

import com.skodin.dtos.StatisticsDto;
import com.skodin.dtos.TimeIntervalDto;
import com.skodin.dtos.TimePeriodDto;
import com.skodin.exceprions.BadRequestException;
import com.skodin.models.AppUser;
import com.skodin.models.Task;
import com.skodin.repositories.AppUserRepository;
import com.skodin.util.mappers.TaskMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class AppUserService {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final AppUserRepository appUserRepository;

    public AppUserService(TaskService taskService, @Lazy TaskMapper taskMapper, AppUserRepository appUserRepository) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.appUserRepository = appUserRepository;
    }

    public <S extends AppUser> S saveAndFlush(S entity) {
        return appUserRepository.saveAndFlush(entity);
    }

    public AppUser findById(UUID uuid) {
        return appUserRepository.findById(uuid).orElseThrow(() -> new BadRequestException("User is not exists"));
    }

    // тут объект должен быть 100 процентов валидный, единственное вопрсо его существования проверяется
    public <S extends AppUser> S update(S entity) {

        findById(entity.getId());   // проверяем существует ли такой пользователь

        entity = appUserRepository.saveAndFlush(entity);

        return entity;
    }

    private List<Task> getTasksForTimePeriod(UUID userId, TimePeriodDto timePeriod) {

        AppUser user = findById(userId);

        log.info(timePeriod);

        LocalDateTime startTime = timePeriod.getStartTime();
        LocalDateTime stopTime = timePeriod.getStopTime();

        Instant instant1 = startTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = stopTime.atZone(ZoneId.systemDefault()).toInstant();

        return taskService.getTasksByUserAndTime(userId, instant1, instant2);
    }

    public List<Task> getTasksByDestination(UUID userId, TimePeriodDto timePeriod) {
        List<Task> tasksForTimePeriod = getTasksForTimePeriod(userId, timePeriod);
        return tasksForTimePeriod.stream().sorted((o1, o2) -> o2.getDuration().compareTo(o1.getDuration())).toList();
    }


    public List<TimeIntervalDto> getIntervalStatistic(UUID userId, TimePeriodDto timePeriod) {
        List<Task> tasksForTimePeriod = getTasksForTimePeriod(userId, timePeriod)
                .stream().sorted(Comparator.comparing(Task::getStartTime)).toList();

        List<TimeIntervalDto> list = new ArrayList<>();

        for (Task task : tasksForTimePeriod) {
            Duration duration = task.getDuration();

            String stringDuration = String.format("%02d:%02d", duration.toHours() % 24, duration.toMinutes() % 60);

            list.add(new TimeIntervalDto(stringDuration, task.getStartTime(), taskMapper.getDto(task)));
        }
        return list;

    }

    public StatisticsDto getWorkHours(UUID userId, TimePeriodDto timePeriod) {
        List<Task> tasksForTimePeriod = getTasksForTimePeriod(userId, timePeriod);

        Duration duration = Duration.ofNanos(0L);

        for (Task task : tasksForTimePeriod) {
            duration = duration.plus(task.getDuration());
        }

        // TODO максимальный предел дни
        return new StatisticsDto(duration.toDays(),
                duration.toHours() % 24,
                duration.toMinutes() % 60);
    }


    public void delete(UUID id) {
        appUserRepository.deleteById(id);
    }
}
