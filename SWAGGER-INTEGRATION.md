# How to Integrate Swagger in Spring Boot Project

This guide provides step-by-step instructions for integrating Swagger (OpenAPI 3) into a Spring Boot project to automatically generate API documentation.

## What is Swagger?

Swagger (now known as OpenAPI) is a specification for machine-readable interface files for describing, producing, consuming, and visualizing RESTful web services. It provides:

- Interactive documentation
- Client SDK generation
- API discoverability

## Step 1: Add Dependencies

Add the SpringDoc OpenAPI dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.4.0</version>
</dependency>
```

For Gradle projects, add to your `build.gradle`:

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0'
```

## Step 2: Configure Swagger

Create a configuration class for Swagger:

```java
package com.nk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce API")
                        .description("Spring Boot REST API for E-Commerce application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("contact@example.com")
                                .url("https://www.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
```

## Step 3: Configure Application Properties

Add Swagger-specific configurations to your `application.properties` file:

```properties
# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

These properties customize the paths for:
- OpenAPI documentation JSON (`/api-docs`)
- Swagger UI interface (`/swagger-ui.html`)

## Step 4: Annotate Controllers and Models

Use OpenAPI annotations to enhance your API documentation:

### Controller Annotations

```java
@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        // Method implementation
    }
    
    // Other endpoints with similar annotations
}
```

### Model Annotations (Optional)

You can also annotate your model classes for better documentation:

```java
@Schema(description = "Product information")
public class Product {

    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;
    
    @Schema(description = "Name of the product", example = "Smartphone", required = true)
    private String name;
    
    // Other fields
}
```

## Step 5: Access Swagger UI

After starting your Spring Boot application, you can access:

1. Swagger UI at: `http://localhost:8080/swagger-ui.html`
2. OpenAPI documentation at: `http://localhost:8080/api-docs`

## What Happens Behind the Scenes

### 1. Auto-Configuration

When you add the SpringDoc OpenAPI dependency, Spring Boot's auto-configuration mechanism:

- Detects the presence of SpringDoc classes
- Automatically configures the necessary beans
- Sets up default endpoints for Swagger UI and OpenAPI documentation

### 2. API Scanning

SpringDoc:
- Scans your application for REST controllers
- Analyzes request mappings, parameters, and return types
- Processes OpenAPI annotations to enhance documentation
- Builds an in-memory representation of your API

### 3. Documentation Generation

When you access the API documentation endpoints:
- SpringDoc generates OpenAPI specification in JSON/YAML format
- The specification describes your API's endpoints, parameters, request/response models, etc.
- Swagger UI reads this specification to render the interactive documentation

### 4. Annotation Processing

- `@Tag` groups operations by resource or concept
- `@Operation` describes individual API endpoints
- `@ApiResponse` documents possible response codes and their meanings
- `@Parameter` provides details about request parameters
- `@Schema` enhances model documentation

## Best Practices

1. **Use Meaningful Descriptions**: Provide clear, concise descriptions for all API endpoints and parameters.

2. **Document Response Codes**: Document all possible HTTP status codes your API might return.

3. **Group Related Operations**: Use tags to group related operations for better organization.

4. **Include Examples**: Provide example values for request parameters and response bodies.

5. **Keep Documentation Updated**: Update Swagger annotations when you change your API.

6. **Secure Swagger UI in Production**: Consider securing or disabling Swagger UI in production environments.

## Troubleshooting

### Common Issues:

1. **Swagger UI Not Loading**:
   - Verify the correct dependency version for your Spring Boot version
   - Check the configured path in application.properties

2. **Missing Endpoints in Documentation**:
   - Ensure controllers have proper Spring annotations (@RestController, @RequestMapping)
   - Check package scanning configuration

3. **Custom Models Not Showing Properly**:
   - Add @Schema annotations to model classes
   - Ensure models have proper getters and setters

## Conclusion

Integrating Swagger with Spring Boot provides powerful, interactive API documentation with minimal effort. By following these steps, you can enhance your API's usability and make it easier for clients to understand and consume your services.