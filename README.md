# Inventory Management System

A Spring Boot-based RESTful API for managing product inventory, including categories, products, and SKUs. The system provides comprehensive CRUD operations with proper validation, error handling, and detailed API documentation.

## Features

- Category Management
- Product Management
- SKU (Stock Keeping Unit) Management
- Pagination and Filtering
- Input Validation
- Error Handling
- Swagger/OpenAPI Documentation
- High Test Coverage (>90%)

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- SpringDoc OpenAPI UI
- JUnit 5 & Mockito
- JaCoCo for Test Coverage
- Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd inventory-management
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, you can access:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Specification: http://localhost:8080/v3/api-docs

## Testing

Run the test suite with coverage report:
```bash
mvn clean test
```

View the JaCoCo coverage report:
```
target/site/jacoco/index.html
```

Current test coverage:
- CategoryServiceImpl: 93.75% line, 100% method
- ProductServiceImpl: 95.65% line, 100% method
- SKUServiceImpl: 100% line, 100% method

## API Endpoints

### Categories
- POST `/api/v1/categories` - Create a new category
- GET `/api/v1/categories` - Get all categories (paginated)
- GET `/api/v1/categories/{id}` - Get a category by ID
- PUT `/api/v1/categories/{id}` - Update a category
- DELETE `/api/v1/categories/{id}` - Delete a category

### Products
- POST `/api/v1/products` - Create a new product
- GET `/api/v1/products` - Get all products (paginated)
- GET `/api/v1/products/{id}` - Get a product by ID
- PUT `/api/v1/products/{id}` - Update a product
- DELETE `/api/v1/products/{id}` - Delete a product

### SKUs
- POST `/api/v1/skus` - Create a new SKU
- GET `/api/v1/skus` - Get all SKUs (paginated)
- GET `/api/v1/skus/{id}` - Get a SKU by ID
- PUT `/api/v1/skus/{id}` - Update a SKU
- DELETE `/api/v1/skus/{id}` - Delete a SKU

## Error Handling

The API uses standard HTTP status codes and returns detailed error messages:
- 400 Bad Request - Invalid input
- 404 Not Found - Resource not found
- 409 Conflict - Resource already exists or constraint violation
- 500 Internal Server Error - Unexpected server error

## Data Model

### Category
- id: Long
- name: String
- products: List<Product>

### Product
- id: Long
- name: String
- description: String
- category: Category
- skus: List<SKU>

### SKU
- id: Long
- product: Product
- color: String
- size: String
- price: BigDecimal
- stockQuantity: Integer

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 