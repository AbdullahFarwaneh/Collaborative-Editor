package com.example.collabeditor.controller;

import com.example.collabeditor.model.Session;
import com.example.collabeditor.model.SessionParticipant;
import com.example.collabeditor.model.User;
import com.example.collabeditor.repository.SessionRepository;
import com.example.collabeditor.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExtrasController {

    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    public ExtrasController(SessionRepository sessionRepository, SessionService sessionService) {
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthenticated"));
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id", user.getId());
        out.put("name", user.getName());
        out.put("email", user.getEmail());
        out.put("createdAt", user.getCreatedAt());
        return ResponseEntity.ok(out);
    }

    @GetMapping("/sessions/{id}/participants")
    public ResponseEntity<?> participants(@PathVariable Long id) {
        List<SessionParticipant> participants = sessionService.getSessionParticipants(id);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/sessions/{id}/code")
    public ResponseEntity<?> sessionCode(@PathVariable Long id) {
        Optional<Session> maybeSession = sessionRepository.findById(id);
        if (maybeSession.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Session not found"));
        }

        Session session = maybeSession.get();
        String live = EditorController.getLiveCode(id);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("sessionId", id);
        out.put("title", session.getTitle());
        out.put("language", session.getLanguage());

        if (live != null) {
            out.put("source", "live");
            out.put("code", live);
        } else if (session.getCode() != null) {
            out.put("source", "db");
            out.put("code", session.getCode());
        } else {
            out.put("source", "none");
            out.put("code", "");
        }

        return ResponseEntity.ok(out);
    }
}

