# Swagger/OpenAPI Documentation

## Accessing the API Documentation

The Three Rivers Bank API now includes comprehensive Swagger/OpenAPI documentation.

### Swagger UI
Access the interactive API documentation at:
- **URL**: http://localhost:8080/swagger-ui.html
- **Alternative URL**: http://localhost:8080/swagger-ui/index.html

### OpenAPI JSON Specification
Access the raw OpenAPI JSON specification at:
- **URL**: http://localhost:8080/v3/api-docs

### OpenAPI YAML Specification
Access the OpenAPI YAML specification at:
- **URL**: http://localhost:8080/v3/api-docs.yaml

## Features

The Swagger UI provides:
- **Interactive API Testing**: Try out API endpoints directly from the browser
- **Request/Response Examples**: View sample requests and responses
- **Schema Documentation**: Detailed information about request and response models
- **Authentication**: Test protected endpoints (if authentication is added)

## API Endpoints

### Accounts
- `GET /api/accounts` - Get all bank accounts
- `GET /api/accounts/{accountNumber}` - Get a specific account by number

### Transactions
- `GET /api/transactions/{accountNumber}` - Get transactions for an account

### Transfers
- `POST /api/transfer` - Transfer funds between accounts

## Example Usage

1. Open http://localhost:8080/swagger-ui.html in your browser
2. Expand an endpoint (e.g., "GET /api/accounts")
3. Click "Try it out"
4. Click "Execute"
5. View the response below

## Dependencies Added

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

This dependency provides:
- Swagger UI integration
- OpenAPI 3.0 specification generation
- Automatic API documentation from Spring annotations
