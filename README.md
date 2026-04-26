# Collaborative-Editor
Real-time collaborative code editor with JWT authentication,  WebSocket live sync,
and sandboxed code execution using Docker.

built with Java and Spring Boot.
Multiple users can edit code simultaneously in shared sessions,
similar to CoderPad or Google Docs for code.

## Features
- Real-time code synchronization using WebSockets and STOMP
- JWT authentication (register, login, protected endpoints)
- Session management (create, join, share sessions)
- Each participant gets a unique cursor color
- Code persisted in PostgreSQL database

## Tech Stack
- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL
- WebSockets + STOMP
 

## Architecture
- REST API for auth and session management
- WebSocket for real-time code broadcasting
- JWT tokens for stateless authentication

## Endpoints
- POST /api/auth/register
- POST /api/auth/login
- POST /api/sessions
- GET  /api/sessions
- GET  /api/sessions/{id}
- POST /api/sessions/{id}/join
