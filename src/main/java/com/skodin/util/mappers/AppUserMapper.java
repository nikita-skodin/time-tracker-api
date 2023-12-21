package com.skodin.util.mappers;

import com.skodin.dtos.AppUserDto;
import com.skodin.models.AppUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserMapper {

    private final ModelMapper mapper;
    private final TaskMapper taskMapper;

    public AppUserDto getDto(AppUser user) {

        if (user == null) {
            throw new NullPointerException("User is null");
        }

        return mapper.map(user, AppUserDto.class);
    }

    public AppUser getEntity(AppUserDto userDto) {

        if (userDto == null) {
            throw new NullPointerException("UserDto is null");
        }

        return mapper.map(userDto, AppUser.class);
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(AppUser.class, AppUserDto.class)
                .addMappings(m -> m.skip(AppUserDto::setTasks)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(AppUserDto.class, AppUser.class)
                .addMappings(m -> m.skip(AppUser::setTasks)).setPostConverter(toEntityConverter());
    }

    private Converter<AppUser, AppUserDto> toDtoConverter() {
        return context -> {

            AppUser source = context.getSource();
            AppUserDto destination = context.getDestination();

            mapSpecificFields(source, destination);

            return context.getDestination();
        };
    }

    private Converter<AppUserDto, AppUser> toEntityConverter() {
        return context -> {

            AppUserDto source = context.getSource();
            AppUser destination = context.getDestination();

            mapSpecificFields(source, destination);

            return context.getDestination();
        };
    }

    private void mapSpecificFields(AppUser source, AppUserDto destination) {
        destination.setTasks(source
                .getTasks()
                .stream()
                .map(taskMapper::getDto)
                .toList());
    }

    private void mapSpecificFields(AppUserDto source, AppUser destination) {
        destination.setTasks(source
                .getTasks()
                .stream()
                .map(taskMapper::getEntity)
                .toList());
    }

}
