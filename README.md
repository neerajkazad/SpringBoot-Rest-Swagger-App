# SpringBoot-Rest-Swagger-App

A Spring Boot REST API application with Swagger integration for product management, featuring optimistic locking for concurrent modifications.

## Table of Contents
- [Project Overview](#project-overview)
- [Project Architecture](#project-architecture)
- [Prerequisites](#prerequisites)
- [Setup and Run](#setup-and-run)
- [API Endpoints](#api-endpoints)
- [Using Swagger UI](#using-swagger-ui)
- [Sample Request/Response Data](#sample-requestresponse-data)
- [Optimistic Locking](#optimistic-locking)
- [What Happens Behind the Scenes](#what-happens-behind-the-scenes)
- [Troubleshooting](#troubleshooting)
- [How to Integrate Swagger in Spring Boot](#how-to-integrate-swagger-in-spring-boot)

## Project Overview

This project is a RESTful API for managing products in an e-commerce application. It provides endpoints for creating, reading, updating, and deleting products, with proper validation and error handling. The API is documented using Swagger (OpenAPI 3), which provides interactive documentation and testing capabilities.

## Project Architecture

The application follows a standard layered architecture:

1. **Controller Layer** (`com.nk.controller`)
   - Handles HTTP requests and responses
   - Validates input data
   - Delegates business logic to the service layer
   - Returns appropriate HTTP status codes

2. **Service Layer** (`com.nk.service`)
   - Contains business logic
   - Implements operations defined in service interfaces
   - Handles transactions and data validation

3. **Repository Layer** (`com.nk.repository`)
   - Interfaces with the database
   - Extends Spring Data JPA repositories
   - Provides CRUD operations for entities

4. **Model Layer** (`com.nk.model`)
   - Defines entity classes mapped to database tables
   - Includes validation annotations
   - Implements optimistic locking with version field

5. **Configuration** (`com.nk.config`)
   - Contains application configuration classes
   - Includes Swagger configuration

### Flow Diagram

```
Client Request → Controller → Service → Repository → Database
                     ↑           ↑          ↑
                     ↓           ↓          ↓
                Response ← DTO/Entity ← Entity
```

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- IDE (IntelliJ IDEA, Eclipse, etc.)

## Setup and Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/SpringBoot-Rest-Swagger-App.git
   cd SpringBoot-Rest-Swagger-App
   ```

2. **Configure the database**

   Update the database configuration in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can run the JAR file directly:
   ```bash
   java -jar target/SpringBoot-Rest-Swagger-App-0.0.1-SNAPSHOT.jar
   ```

5. **Access the application**

   The application will be available at: `http://localhost:8080`

   Swagger UI: `http://localhost:8080/swagger-ui.html`

   OpenAPI documentation: `http://localhost:8080/api-docs`

## API Endpoints

The application provides the following REST endpoints:

| Method | URL                | Description           | Status Codes                                |
|--------|--------------------|-----------------------|---------------------------------------------|
| GET    | /products          | Get all products      | 200 (OK)                                    |
| GET    | /products/{id}     | Get product by ID     | 200 (OK), 404 (Not Found)                   |
| POST   | /products          | Create a new product  | 201 (Created), 400 (Bad Request), 409 (Conflict) |
| PUT    | /products/{id}     | Update a product      | 200 (OK), 400 (Bad Request), 404 (Not Found), 409 (Conflict) |
| DELETE | /products/{id}     | Delete a product      | 204 (No Content), 404 (Not Found), 409 (Conflict) |

## Using Swagger UI

1. Start the application
2. Open your browser and navigate to: `http://localhost:8080/swagger-ui.html`
3. You'll see the Swagger UI interface with all available endpoints
4. Click on an endpoint to expand it
5. Click the "Try it out" button
6. Fill in the required parameters
7. Click "Execute" to send the request
8. View the response

## Sample Request/Response Data

### Create Product (POST /products)

**Request Body:**
```json
{
  "name": "Smartphone",
  "description": "Latest model with high-resolution camera",
  "price": 799.99,
  "quantity": 50
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "version": 0,
  "name": "Smartphone",
  "description": "Latest model with high-resolution camera",
  "price": 799.99,
  "quantity": 50
}
```

### Update Product (PUT /products/{id})

**Request Body:**
```json
{
  "name": "Smartphone Pro",
  "description": "Latest model with high-resolution camera and improved battery",
  "price": 899.99,
  "quantity": 30,
  "version": 0
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "version": 1,
  "name": "Smartphone Pro",
  "description": "Latest model with high-resolution camera and improved battery",
  "price": 899.99,
  "quantity": 30
}
```

**Note:** The `version` field must be included in update requests to ensure optimistic locking works correctly.

## Optimistic Locking

This application implements optimistic locking to handle concurrent modifications of the same resource. The key points are:

1. The `Product` entity includes a `@Version` field that is automatically incremented on each update
2. When updating a product, the client must include the current version number
3. If another client has modified the product in the meantime, the version check will fail
4. The application will return a 409 Conflict status code in case of version mismatch
5. The client should then fetch the latest version and retry the operation

This prevents the "lost update" problem where one client's changes could overwrite another's without warning.

## What Happens Behind the Scenes

### Request Processing Flow

1. **Client Request**
   - Client sends an HTTP request to one of the API endpoints
   - Spring MVC routes the request to the appropriate controller method

2. **Controller Processing**
   - Controller validates the request parameters and body
   - Controller calls the appropriate service method
   - Service implements business logic and calls repository methods
   - Repository interacts with the database using JPA/Hibernate

3. **Database Interaction**
   - Hibernate translates Java objects to SQL queries
   - Database executes the queries and returns results
   - Hibernate maps the results back to Java objects

4. **Response Generation**
   - Service returns the result to the controller
   - Controller wraps the result in a ResponseEntity with appropriate status code
   - Spring MVC converts the ResponseEntity to HTTP response
   - Client receives the HTTP response

### Optimistic Locking Mechanism

1. When a product is first created, its version is set to 0
2. When a product is updated:
   - The client sends the current version number
   - Hibernate adds a WHERE clause to the UPDATE statement to check the version
   - If the version matches, the update succeeds and the version is incremented
   - If the version doesn't match, an ObjectOptimisticLockingFailureException is thrown
   - The controller catches this exception and returns a 409 Conflict status

### Swagger Documentation Generation

1. At application startup, SpringDoc scans all REST controllers
2. It analyzes the controller methods, request mappings, and annotations
3. It builds an in-memory representation of the API
4. When a client accesses the Swagger UI or API docs endpoint:
   - SpringDoc generates the OpenAPI specification
   - The specification is rendered as interactive documentation in the browser

## Troubleshooting

### Common Issues:

1. **Database Connection Errors**
   - Verify MySQL is running
   - Check database credentials in application.properties
   - Ensure the database exists or createDatabaseIfNotExist=true is set

2. **409 Conflict Responses**
   - This is expected when concurrent modifications occur
   - Fetch the latest version of the resource and retry the operation
   - Ensure you're including the version field in update requests

3. **400 Bad Request Responses**
   - Check the validation constraints on the Product entity
   - Ensure all required fields are provided
   - Verify data types match (e.g., price is a number, not a string)

4. **Application Won't Start**
   - Check the console for error messages
   - Verify port 8080 is not already in use
   - Ensure Java 21 is installed and configured

## How to Integrate Swagger in Spring Boot

For detailed instructions on how to integrate Swagger in a Spring Boot project, please see our [Swagger Integration Guide](SWAGGER-INTEGRATION.md).
