# DTO Layer

This package contains Data Transfer Objects (DTOs) for request/response handling.

## Structure
- `request`: Input DTOs with validation
- `response`: Output DTOs with presentation logic

## Responsibilities
- Request/Response data structure
- Input validation
- Data transformation
- API contract definition
- Documentation

## Conventions
- Use separate packages for request/response DTOs
- Include validation annotations in request DTOs
- Use builder pattern when beneficial
- Document all fields
- Keep DTOs immutable when possible
- Include only necessary fields for each operation 