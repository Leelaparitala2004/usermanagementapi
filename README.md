# User Management API

A simple and scalable RESTful API built with **Spring Boot** for managing user data including Create, Read, Update, and Delete (CRUD) operations. Designed to serve as a backend foundation for user-based systems.

---

## 🚀 Features

- Create single or multiple users
- Fetch all users or user by ID
- Update user details
- Delete users by ID
- Validation with Jakarta Bean Validation (`@Valid`)
- Built-in logging with SLF4J
- PostgreSQL database integration
- Swagger or Postman friendly endpoints
- CORS-configurable for frontend integration

---

## 🛠️ Tech Stack

- **Java 17+ / Java 21 / Java 24**
- **Spring Boot 3.5+**
- **Spring Data JPA**
- **PostgreSQL** (or any JDBC-compatible DB)
- **Lombok** *(optional but recommended)*
- **Maven** for dependency management

---

## 📂 Project Structure

```bash
usermanagementapi/
│
├── controller/
│   └── UserController.java
│
├── model/
│   └── User.java
│
├── repository/
│   └── UserRepository.java
│
├── UsermanagementapiApplication.java
└── application.properties
