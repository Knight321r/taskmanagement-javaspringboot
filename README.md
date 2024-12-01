# Task Management API

## Overview

A robust Spring Boot RESTful API for task management with secure user authentication using JWT (JSON Web Tokens).

## 🚀 Technology Stack

- Java 21
- Spring Boot 3.4.0
- Spring Security
- Spring Data JPA
- MySQL Database
- JWT Authentication

## ✨ Features

- User Registration and Authentication
- Secure JWT-based Authorization
- Task CRUD Operations
- User-specific Task Management
- Task Status Tracking

## 📋 Prerequisites

- Java 21
- MySQL Database
- Maven

## 🔧 Installation and Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd task-management
```

### 2. Database Configuration

- Create a MySQL database named `task_management`
- Update database credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

## 🔐 API Endpoints

### Authentication Endpoints

#### Register User
- **URL:** `/auth/register`
- **Method:** `POST`
- **Request Body:**
```json
{
    "username": "string",
    "email": "string",
    "password": "string"
}
```
- **Success Response:** JWT Token
- **Error Responses:**
  - 400: Username already taken

#### Login User
- **URL:** `/auth/login`
- **Method:** `POST`
- **Request Body:**
```json
{
    "username": "string",
    "password": "string"
}
```
- **Success Response:** JWT Token
- **Error Responses:**
  - 401: Invalid credentials
  - 404: User not found

### Task Endpoints

**Note:** All task endpoints require JWT Token in Authorization Header
- **Authorization Header:** `Bearer <jwt_token>`

#### Create Task
- **URL:** `/tasks`
- **Method:** `POST`
- **Request Body:**
```json
{
    "title": "string",
    "description": "string",
    "dueDate": "yyyy-MM-dd HH:mm:ss",
    "status": "PENDING" // Optional
}
```
- **Success Response:** 200 OK

#### Get All Tasks
- **URL:** `/tasks`
- **Method:** `GET`
- **Success Response:** List of user's tasks

#### Get Specific Task
- **URL:** `/tasks/{taskId}`
- **Method:** `GET`
- **Success Response:** Specific task details
- **Error Responses:**
  - 403: No permission
  - 404: Task not found

#### Update Task
- **URL:** `/tasks/{taskId}`
- **Method:** `PUT`
- **Request Body:** Partial or complete task details
- **Success Response:** Updated task details

#### Delete Task
- **URL:** `/tasks/{taskId}`
- **Method:** `DELETE`
- **Success Response:** 200 OK with success message

#### Complete Task
- **URL:** `/tasks/{taskId}/complete`
- **Method:** `PATCH`
- **Success Response:** Task marked as COMPLETED

## 🛡️ Security Features

- Password hashing using BCrypt
- JWT-based stateless authentication
- Role-based access control
- Token-based authorization for all task operations

## 📦 Database Schema

### User Table
- `id` (Primary Key)
- `username` (Unique)
- `email` (Unique)
- `password` (Hashed)

### Task Table
- `id` (Primary Key)
- `title`
- `description`
- `due_date`
- `status`
- `created_at`
- `updated_at`
- `user_id` (Foreign Key to User)

## 🚧 Error Handling

- Custom error responses for various scenarios
- Consistent error message format
- Secure error messages to prevent information leakage

## 💡 Best Practices

- Stateless authentication
- Input validation
- Secure password storage

