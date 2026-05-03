# 🚚 Fleet Management & Route Optimization System

## 📌 Project Overview

This project is a backend REST API system designed to manage fleet operations, including vehicle assignment, driver management, and delivery tasks.

It also includes a **route optimization engine** that helps logistics companies reduce travel distance and improve delivery efficiency.

---

## 🚀 Features

* Vehicle Management (CRUD APIs)
* Driver Management (CRUD APIs)
* Delivery Task Handling
* Route Optimization Engine
* RESTful API Design
* Swagger API Documentation
* Dockerized Application

---

## 🛠 Tech Stack

| Layer      | Technology                  |
| ---------- | --------------------------- |
| Backend    | Java 17, Spring Boot 3      |
| Database   | MySQL                       |
| ORM        | Spring Data JPA (Hibernate) |
| API Docs   | Swagger (Springdoc OpenAPI) |
| Container  | Docker, Docker Compose      |
| Build Tool | Maven                       |

---

## 🐳 Docker Setup

### 🔧 Prerequisites

* Docker installed
* Docker Compose installed

### ▶️ Run the application

docker-compose up --build

### 🌐 Services

* Backend → http://localhost:9090
* Swagger UI → http://localhost:9090/swagger-ui/index.html
* MySQL → localhost:3307

---

## 📡 API Documentation

Interactive API documentation is available using Swagger:

http://localhost:9090/swagger-ui/index.html

---

## 🧠 Route Optimization Algorithm

The system uses a **Greedy (Nearest Neighbor) Algorithm** to determine the optimal delivery route.

### 📌 Steps:

1. Start from the current location (warehouse or vehicle)
2. Find the nearest unvisited delivery location
3. Move to that location
4. Repeat until all locations are visited

### 🎯 Objective:

Minimize total travel distance and improve delivery efficiency.

### ⚙️ Example:

Locations: A → B → C → D

* Start at A
* Nearest → B
* From B → C
* From C → D

Final Route: **A → B → C → D**

### 📊 Complexity:

* Time Complexity: O(n²)
* Efficient for small to medium datasets

### ⚠️ Note:

This approach is fast and simple but may not always produce the globally optimal solution like advanced algorithms.

---

## 📂 Project Structure

src/
├── controller   → REST API endpoints
├── service      → Business logic
├── repository   → Database access layer
├── entity       → JPA entities
└── config       → Configuration files

---

## 🔄 API Endpoints (Sample)

| Method | Endpoint         | Description             |
| ------ | ---------------- | ----------------------- |
| GET    | /vehicles        | Get all vehicles        |
| POST   | /vehicles        | Create a new vehicle    |
| GET    | /drivers         | Get all drivers         |
| POST   | /drivers         | Create a new driver     |
| POST   | /routes/optimize | Optimize delivery route |

---

## ⚙️ Environment Variables

The application uses environment variables for configuration:

* DB_URL
* DB_USER
* DB_PASSWORD
* DB_NAME

---

## 🧪 Testing

APIs can be tested using:

* Swagger UI
* Postman

---

## ✅ Conclusion

This project demonstrates backend development using Spring Boot, REST API design, database integration with MySQL, and containerization using Docker.

It also showcases a basic route optimization algorithm to improve logistics efficiency.

---
