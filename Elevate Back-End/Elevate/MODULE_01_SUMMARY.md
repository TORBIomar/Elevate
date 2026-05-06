# Module 01: User Management - File Structure & Summary

## Project Structure Created

```
src/main/java/ma/emsi/elevate/
├── model/
│   ├── User.java                 # JPA Entity with UserDetails implementation
│   └── UserRole.java             # Enum for role types (CANDIDATE, RECRUITER, ADMIN)
│
├── repository/
│   └── UserRepository.java       # Spring Data JPA Repository
│
├── service/
│   └── UserService.java          # Business Logic Layer
│
├── security/
│   ├── SecurityConfig.java       # Spring Security Configuration
│   └── JwtAuthenticationFilter.java  # JWT Token Filter
│
├── util/
│   └── JwtUtil.java              # JWT Token Generation & Validation
│
├── dto/
│   ├── LoginRequest.java         # Login Request DTO
│   ├── LoginResponse.java        # Login Response DTO with Token
│   ├── RegisterRequest.java      # Registration Request DTO
│   └── UserResponse.java         # User Profile Response DTO
│
└── controller/
    ├── AuthController.java       # Authentication Endpoints (Register, Login)
    └── UserController.java       # User Profile Endpoints

resources/
├── application.properties        # Configuration (DB, JWT, Server)
```

## Files Created Summary

### 1. **Model Layer** (2 files)
- ✅ `User.java` - Complete user entity with security integration
- ✅ `UserRole.java` - Role enumeration

### 2. **Repository Layer** (1 file)
- ✅ `UserRepository.java` - Database access interface

### 3. **Service Layer** (1 file)
- ✅ `UserService.java` - Business logic for user management

### 4. **Security Layer** (2 files)
- ✅ `SecurityConfig.java` - Spring Security configuration
- ✅ `JwtAuthenticationFilter.java` - JWT authentication filter

### 5. **Utility Layer** (1 file)
- ✅ `JwtUtil.java` - JWT token operations

### 6. **DTO Layer** (4 files)
- ✅ `LoginRequest.java`
- ✅ `LoginResponse.java`
- ✅ `RegisterRequest.java`
- ✅ `UserResponse.java`

### 7. **Controller Layer** (2 files)
- ✅ `AuthController.java` - Authentication endpoints
- ✅ `UserController.java` - User profile endpoints

### 8. **Configuration** (1 file)
- ✅ `application.properties` - Database, JWT, and server configuration

### 9. **Documentation**
- ✅ `MODULE_01_README.md` - Complete module documentation

## Total: 14 Java files + 1 Configuration file + 1 README = 16 files created

## Key Features Implemented

✅ User Registration (CANDIDATE, RECRUITER roles)
✅ User Login with JWT Token
✅ Password Encryption (BCrypt)
✅ Role-Based Access Control
✅ User Profile Management
✅ Account Activation/Deactivation
✅ Last Login Tracking
✅ JWT Token Validation & Expiration
✅ CORS Configuration
✅ Error Handling

## Dependencies Added to pom.xml

- ✅ JJWT 0.12.3 (JWT Library)
  - jjwt-api
  - jjwt-impl
  - jjwt-jackson

## API Endpoints Available

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with credentials

### User Profile
- `GET /api/users/profile` - Get current user profile
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/profile` - Update user profile

## Database Setup Required

```sql
CREATE DATABASE elevate_db;
-- Tables will be auto-created by Hibernate (spring.jpa.hibernate.ddl-auto=update)
```

## Next Steps

1. ✅ Module 01 (User Management) - **COMPLETED**
2. ⏳ Module 02 (Job Portal - Offers & Search)
3. ⏳ Module 03 (Application Tracking - CV Upload & Application Status)
4. ⏳ Module 04 (Interviews, Alerts & Statistics)

## How to Test Module 01

### 1. Start the Application
```bash
mvn spring-boot:run
```

### 2. Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "candidate@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+212612345678",
    "role": "CANDIDATE"
  }'
```

### 3. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "candidate@example.com",
    "password": "password123"
  }'
```

### 4. Use Token in Requests
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer {token_from_login}"
```

## Security Notes

⚠️ **Important for Production**:
- Change JWT secret in `application.properties`
- Update database credentials
- Enable HTTPS
- Configure CORS appropriately
- Implement email verification
- Add password reset functionality
- Consider rate limiting for authentication endpoints

