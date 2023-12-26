package com.skodin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
public class SchedulerService {

    private final TaskService taskService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Minsk")
    public void scheduleCronExpressionTask() throws InterruptedException {

        taskService.clear();

    }

}
