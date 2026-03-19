package com.example.collabeditor.controller;

import com.example.collabeditor.model.Session;
import com.example.collabeditor.repository.SessionRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class EditorController {

    private final SessionRepository sessionRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public EditorController(SessionRepository sessionRepository,
                            SimpMessagingTemplate messagingTemplate) {
        this.sessionRepository = sessionRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/editor/{sessionId}")
    public void handleCodeChange
            (@DestinationVariable Long sessionId,String content) {

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow();
                session.setCode(content);
               sessionRepository.save(session);

        messagingTemplate.convertAndSend(
                "/topic/editor/" + sessionId,
                content
        );
    }
}