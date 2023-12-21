package com.skodin.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "app_user_time_tracker")
public class AppUser {

    @Id
    @GeneratedValue
    UUID id;

    String username;

    @OneToMany(mappedBy = "user")
    List<Task> tasks;

    public List<Task> getTasks() {
        return tasks == null ? new ArrayList<>() : tasks;
    }
}

