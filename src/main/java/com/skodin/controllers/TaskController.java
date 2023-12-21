package com.skodin.controllers;

import com.skodin.dtos.TaskDto;
import com.skodin.models.Task;
import com.skodin.services.TaskService;
import com.skodin.util.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto dto) {
        Task task = taskService.saveAndFlush(taskMapper.getEntity(dto));
        return ResponseEntity.ok(taskMapper.getDto(task));
    }

    @PatchMapping("/start/{task_id}")
    public ResponseEntity<TaskDto> timeStart(@PathVariable("task_id") UUID id) {
        Task task = taskService.startTime(id);
        return ResponseEntity.ok(taskMapper.getDto(task));
    }

    @PatchMapping("/stop/{task_id}")
    public ResponseEntity<TaskDto> timeStop(@PathVariable("task_id") UUID id) {
        Task task = taskService.stopTime(id);
        return ResponseEntity.ok(taskMapper.getDto(task));
    }

}
