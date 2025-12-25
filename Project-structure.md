# Inventory Management System - Project Structure

## Overview
This is a Spring Boot application for managing inventory, including products, categories, and SKUs. The project follows a clean architecture pattern with clear separation of concerns and domain-driven design principles.

## Architectural Layers

### 1. Presentation Layer (API)
**Location**: `controller/`
- Handles HTTP requests and responses
- Manages API versioning and routing
- Performs basic request validation
- Converts DTOs to/from domain objects
- No business logic - delegates to services
- Handles HTTP-specific concerns only

### 2. Business Layer (Service)
**Location**: `service/`
- Contains core business logic and rules
- Orchestrates operations across multiple repositories
- Manages transactions and data consistency
- Implements business validations
- Independent of HTTP/presentation concerns
- Operates on domain models, not DTOs

### 3. Data Access Layer (Repository)
**Location**: `repository/`
- Handles database operations
- Implements data access patterns
- Manages entity relationships
- Handles data persistence concerns
- No business logic
- Pure data access operations

## Module Responsibilities

### 1. Configuration (`config/`)
- Application-wide configurations
- Bean definitions and customizations
- External service configurations
- OpenAPI/Swagger setup
- Security configurations

### 2. Controllers (`controller/`)
- REST endpoint definitions
- Request/Response handling
- Input validation triggering
- HTTP-specific error handling
- API versioning
- Resource mapping

### 3. Domain (`domain/`)
- Core business concepts
- Business rules
- Domain events
- Value objects
- Domain-specific operations

### 4. DTOs (`dto/`)
**Request DTOs** (`request/`)
- Input validation rules
- Request parameter grouping
- API version-specific structures

**Response DTOs** (`response/`)
- Structured API responses
- Error response formatting
- Data transformation rules

### 5. Exception Handling (`exception/`)
- Custom exception definitions
- Global error handling
- Error response formatting
- Exception to HTTP status mapping
- Business rule violation handling

### 6. Mappers (`mapper/`)
- DTO ↔ Domain model conversions
- Object transformation rules
- Data format standardization
- Response formatting

### 7. Models (`model/`)
- JPA entities
- Database schema representation
- Entity relationships
- Data validation rules
- Database constraints

### 8. Repositories (`repository/`)
- Data access interfaces
- Custom query definitions
- Database operation patterns
- Data persistence logic
- Entity relationship management

### 9. Services (`service/`)
**Service Interfaces**
- Business operation contracts
- Transaction boundaries
- Service-level abstractions

**Service Implementations** (`impl/`)
- Business logic implementation
- Transaction management
- Cross-cutting concerns
- Domain rule enforcement
- Service orchestration

**Validation Services** (`validation/`)
- Complex validation rules
- Business rule validation
- Cross-entity validation
- Validation orchestration

### 10. Validation (`validation/`)
- Custom validation annotations
- Validation implementations
- Reusable validation rules
- Input sanitization rules

## Logical Boundaries and Interactions

### Controller → Service
- Controllers depend on service interfaces
- DTOs are transformed to domain objects
- Validation annotations are processed
- HTTP concerns don't leak into services

### Service → Repository
- Services use repositories for data access
- Transaction boundaries are defined
- Domain objects are transformed to entities
- Business rules are enforced before persistence

### Cross-Cutting Concerns
- Validation occurs at multiple levels:
  - DTOs: Input validation
  - Services: Business rule validation
  - Entities: Data integrity validation
- Exception handling is layered:
  - Repository: Data access exceptions
  - Service: Business exceptions
  - Controller: HTTP-specific exceptions

## Project Layout

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── inventory/
│   │           ├── config/          # Application configurations
│   │           ├── controller/      # REST endpoints (Presentation Layer)
│   │           ├── domain/          # Core business concepts
│   │           ├── dto/             # Data Transfer Objects
│   │           │   ├── request/     # Input DTOs
│   │           │   └── response/    # Output DTOs
│   │           ├── exception/       # Error handling
│   │           ├── mapper/          # Object transformations
│   │           ├── model/           # Database entities
│   │           ├── repository/      # Data Access Layer
│   │           ├── service/         # Business Layer
│   │           │   ├── impl/        # Business logic implementations
│   │           │   └── validation/  # Business rule validation
│   │           └── validation/      # Input validation
│   └── resources/
│       └── application.properties   # Application configuration
└── test/                           # Mirror of main structure for tests
```

## Key Features
- Clean Architecture principles
- Domain-Driven Design concepts
- Layered validation strategy
- Comprehensive exception handling
- DTO pattern for API contracts
- Separation of concerns
- Modular design
- Comprehensive test coverage
- OpenAPI documentation

## Build and Dependencies
The project uses Maven for dependency management and build automation, configured in `pom.xml`. 