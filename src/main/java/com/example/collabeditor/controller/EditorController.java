package com.example.collabeditor.controller;

import com.example.collabeditor.model.EditorMessage;
import com.example.collabeditor.model.Session;
import com.example.collabeditor.repository.SessionRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/*public class EditorController {

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
}*/
@Controller
public class EditorController {

    private final SimpMessagingTemplate messagingTemplate;

    private static final Map<Long, String> liveCodeCache = new ConcurrentHashMap<>();

    public EditorController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/editor/{sessionId}")
    public void handleEditorAction(@DestinationVariable Long sessionId, EditorMessage message) {

        if ("EDIT".equals(message.getType())) {

            liveCodeCache.put(sessionId, message.getContent());


            messagingTemplate.convertAndSend("/topic/editor/" + sessionId, message);
        }
        else if ("CURSOR".equals(message.getType())) {

            messagingTemplate.convertAndSend("/topic/cursor/" + sessionId, message);
        }
    }

    public static String getLiveCode(Long sessionId) {
        return liveCodeCache.get(sessionId);
    }
}