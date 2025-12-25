# Service Layer

This package contains business logic and transaction management.

## Structure
- Interface definitions in root package
- Implementations in `impl` subpackage

## Responsibilities
- Business logic implementation
- Transaction management
- Data validation
- Entity to DTO mapping
- Error handling
- Business rules enforcement

## Conventions
- Use interfaces for service definitions
- Implement `@Transactional` management
- Follow Single Responsibility Principle
- Use constructor injection with `@RequiredArgsConstructor`
- Document complex business logic
- Handle business exceptions 