# Module 1: Gestion des Utilisateurs (User Management)

## Overview
Module 1 handles user authentication, registration, account management, and role-based access control for the Elevate job portal platform.

## Features
- **User Registration**: Support for Candidates, Recruiters, and Admins
- **User Login**: Email and password-based authentication with JWT tokens
- **JWT Token Management**: Secure token generation and validation
- **Role-Based Access Control**: Three roles - Candidate, Recruiter, Admin
- **User Profile Management**: Update user information
- **Account Activation/Deactivation**: Control user account status

## Architecture

### Entities
- **User**: Main entity representing a user in the system
- **UserRole**: Enum for user roles (CANDIDATE, RECRUITER, ADMIN)

### Components

#### 1. Model Layer
- `User.java`: JPA entity with UserDetails implementation
- `UserRole.java`: Enum for role management

#### 2. Repository Layer
- `UserRepository.java`: Spring Data JPA repository for User entity

#### 3. Service Layer
- `UserService.java`: Business logic for user operations
  - User registration with password encryption
  - User authentication
  - Profile management
  - Account activation/deactivation

#### 4. Security Layer
- `SecurityConfig.java`: Spring Security configuration
  - JWT authentication provider
  - Security filter chain
  - CORS configuration
- `JwtAuthenticationFilter.java`: JWT token extraction and validation
- `JwtUtil.java`: JWT token generation and validation

#### 5. Controller Layer
- `AuthController.java`: Authentication endpoints
  - POST `/api/auth/register`: User registration
  - POST `/api/auth/login`: User login
- `UserController.java`: User profile endpoints
  - GET `/api/users/profile`: Get current user profile
  - GET `/api/users/{id}`: Get user by ID
  - PUT `/api/users/profile`: Update user profile

#### 6. DTO Layer
- `LoginRequest.java`: Login request payload
- `LoginResponse.java`: Login response with JWT token
- `RegisterRequest.java`: Registration request payload
- `UserResponse.java`: User data response payload

## API Endpoints

### Authentication

#### 1. Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+212612345678",
  "role": "CANDIDATE"
}

Response (201 Created):
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+212612345678",
  "role": "CANDIDATE",
  "profilePictureUrl": null,
  "isActive": true,
  "createdAt": "2024-04-19 10:30:45",
  "updatedAt": "2024-04-19 10:30:45"
}
```

#### 2. Login User
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "CANDIDATE"
}
```

### User Profile

#### 3. Get Current User Profile
```
GET /api/users/profile
Authorization: Bearer {token}

Response (200 OK):
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+212612345678",
  "role": "CANDIDATE",
  "profilePictureUrl": null,
  "isActive": true,
  "createdAt": "2024-04-19 10:30:45",
  "updatedAt": "2024-04-19 10:30:45"
}
```

#### 4. Get User by ID
```
GET /api/users/{id}
Authorization: Bearer {token}

Response (200 OK):
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+212612345678",
  "role": "CANDIDATE",
  "profilePictureUrl": null,
  "isActive": true,
  "createdAt": "2024-04-19 10:30:45",
  "updatedAt": "2024-04-19 10:30:45"
}
```

#### 5. Update User Profile
```
PUT /api/users/profile
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+212687654321",
  "profilePictureUrl": "https://example.com/profile.jpg"
}

Response (200 OK):
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+212687654321",
  "role": "CANDIDATE",
  "profilePictureUrl": "https://example.com/profile.jpg",
  "isActive": true,
  "createdAt": "2024-04-19 10:30:45",
  "updatedAt": "2024-04-19 11:15:30"
}
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(20),
  role VARCHAR(50) NOT NULL,
  profile_picture_url VARCHAR(500),
  is_active BOOLEAN DEFAULT TRUE,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  last_login DATETIME
);
```

## Security Features

1. **Password Encryption**: Uses BCrypt for secure password hashing
2. **JWT Authentication**: Token-based stateless authentication
3. **CORS Configuration**: Cross-Origin requests allowed
4. **Role-Based Access Control**: Different permissions based on user role
5. **Account Status Management**: Active/Inactive user accounts

## Configuration

### JWT Configuration (application.properties)
```properties
jwt.secret=elevate-secret-key-that-should-be-changed-in-production-environment-with-at-least-32-characters
jwt.expiration=86400000  # 24 hours in milliseconds
```

### Database Configuration (application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elevate_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Dependencies
- Spring Boot 4.0.5
- Spring Security
- Spring Data JPA
- MySQL Connector
- Lombok
- JJWT (JWT library)

## Installation & Setup

1. **Database Setup**:
   - Create a MySQL database named `elevate_db`
   - Update database credentials in `application.properties`

2. **Build Project**:
   ```bash
   mvn clean install
   ```

3. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Test Endpoints**:
   - Use Postman or curl to test the API endpoints

## Error Handling

All endpoints return appropriate HTTP status codes:
- `200 OK`: Successful operation
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication failed or token invalid
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Future Enhancements
- Email verification for registration
- Password reset functionality
- Two-factor authentication
- Social login integration
- User search and filtering
- Admin user management endpoints

