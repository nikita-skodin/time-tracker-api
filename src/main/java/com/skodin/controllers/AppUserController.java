package com.skodin.controllers;

import com.skodin.dtos.*;
import com.skodin.models.AppUser;
import com.skodin.services.AppUserService;
import com.skodin.services.TaskService;
import com.skodin.util.mappers.AppUserMapper;
import com.skodin.util.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserMapper appUserMapper;
    private final TaskMapper taskMapper;
    private final AppUserService appUserService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody AppUserDto appUserDto) {
        AppUser appUser = appUserMapper.getEntity(appUserDto);
        AppUser user = appUserService.saveAndFlush(appUser);
        return ResponseEntity.ok(appUserMapper.getDto(user));
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<AppUserDto> updateUser(@RequestBody AppUserDto appUserDto,
                                                 @PathVariable("user_id") UUID userId) {
        appUserDto.setId(userId);
        AppUser appUser = appUserMapper.getEntity(appUserDto);
        AppUser user = appUserService.update(appUser);
        return ResponseEntity.ok(appUserMapper.getDto(user));
    }

    /**
     * показать все трудозатраты пользователя Y
     * за период N..M в виде связного списка Задача - Сумма затраченного времени в виде (чч:мм)
     * с сортировкой от большего к меньшему (для ответа на вопрос, На какие задачи я потратил больше времени);
     */
    @GetMapping("/{user_id}/employment_statistics")
    public List<TaskDto> getTasksByDestination(@PathVariable("user_id") UUID id,
                                               @RequestBody TimePeriodDto timePeriod) {
        return appUserService.getTasksByDestination(id, timePeriod).stream().map(taskMapper::getDto).toList();
    }

    @GetMapping("/{user_id}/interval_statistics")
    public List<TimeIntervalDto> getIntervalStatistic(@PathVariable("user_id") UUID id,
                                                      @RequestBody TimePeriodDto timePeriod) {
        return appUserService.getIntervalStatistic(id, timePeriod);
    }

    @GetMapping("/{user_id}/work_hours")
    public StatisticsDto getWorkHours(@PathVariable("user_id") UUID id,
                                      @RequestBody TimePeriodDto timePeriod) {
        return appUserService.getWorkHours(id, timePeriod);
    }

    @PostMapping("/{user_id}/clean")
    public boolean cleanUser(@PathVariable("user_id") UUID id) {
        taskService.clearUser(appUserService.findById(id));
        return true;
    }

    @DeleteMapping("/{user_id}")
    public boolean deleteUser(@PathVariable("user_id") UUID id) {
        appUserService.delete(id);
        return true;
    }
}
