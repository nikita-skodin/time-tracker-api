package com.skodin.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skodin.models.Task;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    UUID id;

    String username;

    List<TaskDto> tasks;

    public List<TaskDto> getTasks() {
        return tasks == null ? new ArrayList<>() : tasks;
    }
}
