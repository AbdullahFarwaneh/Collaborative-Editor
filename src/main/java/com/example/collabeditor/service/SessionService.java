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
private SessionRepository sessionRepository;
private  SessionParticipantRepository sessionParticipantRepository;


public SessionService(SessionRepository sessionRepository,SessionParticipantRepository sessionParticipantRepository){
    this.sessionRepository=sessionRepository;
    this.sessionParticipantRepository=sessionParticipantRepository;

}

public Session createSession (String title ,String language,User creator){

    Session session=new Session();
    session.setTitle(title);
    session.setLanguage(language);
    session.setCreatedBy(creator);

    Session saved =sessionRepository.save(session);

SessionParticipant sessionParticipant=new SessionParticipant();
sessionParticipant.setSession(saved);
sessionParticipant.setUser(creator);
sessionParticipant.setCursorColor("blue");
sessionParticipantRepository.save(sessionParticipant);

return saved;
}

public List<SessionParticipant> getSessionParticipants(Long session_id) {   return sessionParticipantRepository.findBySessionId(session_id);}

public List<Session> getMySessions(User user){
    return sessionRepository.findByCreatedById(user.getId());

}
public SessionParticipant joinSession( Long session_id,User user){
    SessionParticipant sessionParticipant=new SessionParticipant();
    sessionParticipant.setSession(sessionRepository.findById(session_id).orElseThrow());
    sessionParticipant.setUser(user);
    sessionParticipant.setCursorColor(AssignColor(session_id));
    return sessionParticipantRepository.save(sessionParticipant);

}

private String AssignColor(Long session_id){
    String [] arr={"Blue","Red","Green","Yellow","Purple"};
List<SessionParticipant> participants =sessionParticipantRepository.findBySessionId(session_id);
return arr[participants.size()% arr.length];
}


}