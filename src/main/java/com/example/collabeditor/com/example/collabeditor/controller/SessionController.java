package com.example.collabeditor.controller;

import com.example.collabeditor.model.Session;
import com.example.collabeditor.model.SessionParticipant;
import com.example.collabeditor.model.User;
import com.example.collabeditor.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String language = body.get("language");
        User user = getCurrentUser();
        Session session = sessionService.createSession(title, language, user);
        return ResponseEntity.ok(session);
    }

    @GetMapping
    public ResponseEntity<?> getMySessions() {
        User user = getCurrentUser();
        List<Session> sessions = sessionService.getUserSessions(user);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionParticipants(@PathVariable Long id) {
        List<SessionParticipant> participants =
                sessionService.getSessionParticipants(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinSession(@PathVariable Long id) {
        User user = getCurrentUser();
        SessionParticipant participant = sessionService.joinSession(id, user);
        return ResponseEntity.ok(participant);
    }
}