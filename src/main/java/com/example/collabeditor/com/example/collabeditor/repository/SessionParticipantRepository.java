package com.example.collabeditor.repository;

import com.example.collabeditor.model.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SessionParticipantRepository
        extends JpaRepository<SessionParticipant, Long> {

    List<SessionParticipant> findBySessionId(Long sessionId);
    List<SessionParticipant> findByUserId(Long userId);
}