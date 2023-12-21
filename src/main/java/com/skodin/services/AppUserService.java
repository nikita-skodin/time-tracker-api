package com.skodin.services;

import com.skodin.dtos.TimePeriodDto;
import com.skodin.exceprions.BadRequestException;
import com.skodin.models.AppUser;
import com.skodin.models.Task;
import com.skodin.repositories.AppUserRepository;
import com.skodin.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppUserService {

    private final TaskService taskService;
    private final AppUserRepository appUserRepository;

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

    public List<Task> getTasksForTimePeriod(UUID id, TimePeriodDto timePeriod) {

        AppUser user = findById(id);

        log.info(timePeriod);

        LocalDateTime startTime = timePeriod.getStartTime();
        LocalDateTime stopTime = timePeriod.getStopTime();

        Instant instant1 = startTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = stopTime.atZone(ZoneId.systemDefault()).toInstant();

        return taskService.getTasksByUserAndTime(id, instant1, instant2);
    }
}
