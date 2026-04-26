package com.example.collabeditor.service;

import com.example.collabeditor.repository.SessionRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class AutoSaveService {

    private final SessionRepository sessionRepository;

    public AutoSaveService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    @Scheduled(fixedRate = 30000)
    public void saveAllSessionsToDb() {
        System.out.println("Auto-saving active sessions to Database...");
    }
}