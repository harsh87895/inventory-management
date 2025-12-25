# Repository Layer

This package contains Spring Data JPA repositories for database operations.

## Responsibilities
- Database access
- CRUD operations
- Custom queries
- Query optimization
- Data persistence

## Conventions
- Extend `JpaRepository` or `CrudRepository`
- Use meaningful method names following Spring Data naming conventions
- Document complex queries
- Use `@Query` for custom queries when method names become too complex
- Implement pagination where needed
- Use projections for optimized data fetching 