# Controller Layer

This package contains REST controllers that handle HTTP requests and responses.

## Responsibilities
- Handle HTTP requests
- Input validation
- URL mapping
- Response formatting
- Swagger/OpenAPI documentation
- Error handling delegation

## Conventions
- Use `@RestController` annotation
- Group related endpoints under the same controller
- Version APIs using `/api/v1/` prefix
- Use proper HTTP methods (GET, POST, PUT, DELETE)
- Include Swagger documentation
- Return appropriate HTTP status codes 