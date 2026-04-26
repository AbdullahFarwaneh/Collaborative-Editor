package com.example.collabeditor.controller;

import com.example.collabeditor.model.Session;
import com.example.collabeditor.model.SessionParticipant;
import com.example.collabeditor.model.User;
import com.example.collabeditor.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You have to login first !");
        }
        return user;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionParticipants(@PathVariable Long id) {
        User user = getCurrentUser();



        List<SessionParticipant> participants = sessionService.getSessionParticipants(id);

        if (participants == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("session not found or finished");
        }

        return ResponseEntity.ok(participants);
    }


    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinSession(@PathVariable Long id) {
        User user = getCurrentUser();
        try {
            SessionParticipant participant = sessionService.joinSession(id, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(participant);
        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Map<String, String> body) {
        String title = body.get("title");
        String language = body.get("language");

        if (title == null || title.isEmpty()) {
            return ResponseEntity.badRequest().body("Session title is taken ");
        }

        User user = getCurrentUser();
        Session session = sessionService.createSession(title, language, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping
    public ResponseEntity<?> getMySessions() {
        User user = getCurrentUser();
        List<Session> sessions = sessionService.getMySessions(user);
        return ResponseEntity.ok(sessions);
    }
}