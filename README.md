# Star Wars REST API

A Spring Boot REST API that integrates with the public SWAPI service and exposes character-related endpoints, including caching and a mocked authentication flow.

---

## Quickstart

### Prerequisites
- Java 21+
- Maven
- Spring Boot 3.5.10
- Spotless Maven Plugin (code formatting)

### Run the application
```bash
mvn spring-boot:run
```

The application will start on:

`http://localhost:8080`

## Running Tests
To run all unit and integration tests:

```bash
mvn test
```

The test suite covers:

- Authentication flow (login and access to protected endpoints)
- Caching behavior for the `/people` endpoint

## Design Decisions & Trade-offs
### In-memory authentication
- Access and refresh tokens are stored in memory using `ConcurrentHashMap`.
- Keeps the implementation simple and easy to understand.
- Trade-off: tokens are lost on application restart and this approach is not production-ready.

### Interceptor-based authorization
- Authentication is enforced using a custom `HandlerInterceptor`, without Spring Security.
- Reduces boilerplate and keeps the focus on core logic.
- Trade-off: advanced security features (roles, permissions, filters) are intentionally omitted.

### Caching with Caffeine
- The `/people` endpoint is cached by page number using Spring Cache abstraction.
- Prevents unnecessary calls to the external SWAPI service.
- Trade-off: cache hit logging is limited due to how `@Cacheable` works internally.

### Explicit validation and error handling
- Request parameters are validated directly in controllers.
- Error responses use simple textual messages instead of a dedicated error DTO, for clarity and brevity.

## Authentication Flow
The API uses a simplified token-based authentication mechanism:

- The client calls `POST /auth/login` and receives an `accessToken` and `refreshToken`.
- The `accessToken` is sent in the `Authorization` header using the Bearer scheme when accessing protected endpoints.
- If the access token is missing or invalid, the server responds with `401 Unauthorized`.
- The client can call `POST /auth/refresh` with a valid refresh token to obtain a new access token.
- Calling `POST /auth/logout` invalidates the access token and ends the session.

## API Request Examples
Base URL: `http://localhost:8080`

### People
Get a paged list of people:
```bash
curl "http://localhost:8080/people?page=1"
```

Get a single person by id:
```bash
curl "http://localhost:8080/people/1"
```

### Authentication
Login:
```bash
curl -X POST "http://localhost:8080/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"user\":\"demo-user\"}"
```

Refresh access token:
```bash
curl -X POST "http://localhost:8080/auth/refresh" \
  -H "X-Refresh-Token: <refresh-token>"
```

Logout:
```bash
curl -X POST "http://localhost:8080/auth/logout" \
  -H "Authorization: Bearer <access-token>"
```

### Favourites (protected)
```bash
curl "http://localhost:8080/favourites" \
  -H "Authorization: Bearer <access-token>"
```
