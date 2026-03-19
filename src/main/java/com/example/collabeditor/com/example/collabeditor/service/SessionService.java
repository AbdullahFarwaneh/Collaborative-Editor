package com.example.collabeditor.service;

import com.example.collabeditor.model.Session;
import com.example.collabeditor.model.SessionParticipant;
import com.example.collabeditor.model.User;
import com.example.collabeditor.repository.SessionParticipantRepository;
import com.example.collabeditor.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;

    public SessionService(SessionRepository sessionRepository,
                          SessionParticipantRepository participantRepository) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
    }

    public Session createSession(String title, String language, User creator) {
        Session session = new Session();
        session.setTitle(title);
        session.setLanguage(language);
        session.setCreatedBy(creator);

        Session saved = sessionRepository.save(session);

        SessionParticipant participant = new SessionParticipant();
        participant.setSession(saved);
        participant.setUser(creator);
        participant.setCursorColor("blue");
        participantRepository.save(participant);

        return saved;
    }

    public List<Session> getUserSessions(User user) {
        return sessionRepository.findByCreatedById(user.getId());
    }

    public List<SessionParticipant> getSessionParticipants(Long sessionId) {
        return participantRepository.findBySessionId(sessionId);
    }

    public SessionParticipant joinSession(Long sessionId, User user) {
        SessionParticipant participant = new SessionParticipant();
        participant.setSession(sessionRepository.findById(sessionId).orElseThrow());
        participant.setUser(user);
        participant.setCursorColor(assignColor(sessionId));
        return participantRepository.save(participant);
    }

    private String assignColor(Long sessionId) {
        List<SessionParticipant> existing=
                participantRepository.findBySessionId(sessionId);
        String[] colors = {"blue", "red","green","purple","orange"};
        return colors[existing.size() % colors.length];
    }
}