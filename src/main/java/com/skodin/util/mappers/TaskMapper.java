package com.skodin.util.mappers;

import com.skodin.dtos.TaskDto;
import com.skodin.models.Task;
import com.skodin.services.AppUserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ModelMapper mapper;
    private final AppUserService appUserService;

    public TaskDto getDto(Task task) {

        if (task == null){
            throw new NullPointerException("Task is null");
        }

        return mapper.map(task, TaskDto.class);
    }

    public Task getEntity(TaskDto taskDto) {

        if (taskDto == null){
            throw new NullPointerException("TaskDto is null");
        }

        return mapper.map(taskDto, Task.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Task.class, TaskDto.class)
                .addMappings(m -> m.skip(TaskDto::setUserId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(TaskDto.class, Task.class)
                .addMappings(m -> m.skip(Task::setUser)).setPostConverter(toEntityConverter());
    }

    private Converter<TaskDto, Task> toEntityConverter() {
        return context -> {
            TaskDto source = context.getSource();
            Task destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<Task, TaskDto> toDtoConverter() {
        return context -> {
            Task source = context.getSource();
            TaskDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(TaskDto source, Task destination) {
        destination.setUser(appUserService.findById(source.getUserId()));
    }

    private void mapSpecificFields(Task source, TaskDto destination) {
        destination.setUserId(source.getId());
    }

}
