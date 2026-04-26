# Backend Guide (Simple)

This project is a Spring Boot backend (Java 17 + Maven) that provides:
- JWT auth (register/login)
- Session management (create/list/join)
- WebSocket/STOMP live updates for editor content + cursor positions
- PostgreSQL persistence (configured in `src/main/resources/application.properties`)

## 1) How to run it

### A) Start PostgreSQL
You need a database called `collabeditor` and a user/password that match:
`src/main/resources/application.properties`

Default in this repo:
- DB: `collabeditor`
- User: `postgres`
- Password: `1234`

### B) Run Spring Boot
From the project folder:
- `.\mvnw spring-boot:run`

Backend should start on:
- `http://localhost:8080`

## 2) Open the simple frontend

When the app is running, open:
- `http://localhost:8080/editor.html`

That page is a single-file frontend that can:
- Register + login (JWT stored in the browser)
- Create sessions, list your sessions, join a session
- Live edit using WebSockets (STOMP)

## 3) API endpoints (REST)

### Auth
- `POST /api/auth/register`
  - Body: `{ "name": "...", "email": "...", "password": "..." }`
- `POST /api/auth/login`
  - Body: `{ "email": "...", "password": "..." }`
  - Response: `{ "token": "..." }`

### Sessions (requires `Authorization: Bearer <token>`)
- `POST /api/sessions`
  - Body: `{ "title": "...", "language": "java|javascript|python|..." }`
- `GET /api/sessions`
  - Lists sessions created by the current user
- `POST /api/sessions/{id}/join`
  - Joins as a participant (assigns a cursor color)
- `GET /api/sessions/{id}`
  - Returns participants for that session

### Extras (added endpoints)
- `GET /api/me`
  - Returns basic info about the current authenticated user
- `GET /api/sessions/{id}/code`
  - Returns the latest known code (prefers live cache, falls back to DB)
- `GET /api/sessions/{id}/participants`
  - Same data as `GET /api/sessions/{id}`, but explicit naming
- `GET /api/health`
  - Simple health response

## 4) WebSockets (STOMP)

WebSocket endpoint:
- `GET /ws` (SockJS is enabled)

Messaging prefixes:
- Client sends to: `/app/...`
- Client subscribes to: `/topic/...`

Editor topics used by the frontend:
- Subscribe: `/topic/editor/{sessionId}`
- Subscribe: `/topic/cursor/{sessionId}`
- Send: `/app/editor/{sessionId}`

Message format (JSON):
```json
{
  "sender": "you@example.com",
  "content": "code text here",
  "type": "EDIT|CURSOR",
  "line": 12,
  "column": 4
}
```

