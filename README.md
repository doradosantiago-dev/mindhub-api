# MindHub Core Engineering

> MindHub high-availability server-side logic and secure data orchestration.

<p align="left">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
</p>

Orchestration and automation of the operational workflow for publications within the system, acting as the core engine for data persistence and content validation. Its architecture ensures that every data entry strictly adheres to integrity standards, JWT-based security protocols, and the administrative governance policies defined at the service core.

## Tech Stack & Architecture Overview

### Core Technology Stack

The MindHub engine is built upon a modern, high-performance stack, specifically engineered to ensure scalability and the security of all publications.

| Category | Technology | Role in Ecosystem |
| :--- | :--- | :--- |
| **Runtime** | **Java 21 (LTS)** | Modern language features & virtual threads ready. |
| **Framework** | **Spring Boot 3.4** | Core container and dependency injection. |
| **Security** | **Spring Security 6.4** | Robust authentication & RBAC implementation. |
| **Identity** | **JJWT (JSON Web Token)** | Stateless session management & secure data exchange. |
| **Persistence** | **Spring Data JPA** | Object-Relational Mapping and data integrity. |
| **Database** | **PostgreSQL 18** | Relational storage for high-availability workloads. |
| **API Docs** | **OpenAPI / Swagger** | Interactive API contract and documentation. |
| **Validation** | **Bean Validation** | Strict enforcement of business data constraints. |
| **Build Tool** | **Maven** | Dependency management and build lifecycle automation. |

### Layered Architecture & Project Structure

The system follows a Layered Architecture pattern, ensuring effective decoupling between data persistence, business logic, and API endpoints.

```text
src/main/java/com/mindhub/core/
├── 📁 security      # JWT Implementation, Auth Filters & Providers
├── 📁 config        # General System & Global Bean Configurations
├── 📁 controller    # REST API Endpoints
├── 📁 service       # Business Logic & Orchestration
├── 📁 repository    # Data Access Layer (JPA)
├── 📁 model         # Persistence Entities
├── 📁 dto           # Data Transfer Objects
├── 📁 mapper        # Entity-to-DTO Conversion Logic
└── 📁 exception     # Centralized Error Handling
```

**Key Architectural Principles**

- **Separation of Concerns (SoC):** Each layer has a unique, well-defined responsibility to facilitate maintenance and scalability.

- **DTO & Mapper Pattern:** Strict entity decoupling using specialized mappers to protect domain model integrity and optimize API responses.

- **Global Exception Handling:** Centralized error management ensuring consistent HTTP status codes and professional API feedback.

- **Stateless Security:** Dedicated security layer focused on JWT validation, keeping core logic independent of the authentication mechanism.

### Core Implementation & Security

The system manages the full publication lifecycle through a robust security model, combining business logic with high-level protection.

* **Stateless Authentication:** Secured with **JWT (JSON Web Tokens)** for scalable and decoupled session management.
* **Role-Based Access Control (RBAC):** Granular permissions ensuring users only manage their own data, while **ADMIN** roles maintain global governance.
* **Publication Management:** Real-time handling of technical posts with strict server-side validation to ensure data consistency.
* **Data Integrity:** Centralized exception handling and specialized Mappers to ensure a clean and secure data flow between layers.

### Database & API Reference

* **Data Modeling:** Optimized relational structure managed through **Spring Data JPA**, ensuring strict referential integrity and efficient mapping between entities and tables.
* **API Documentation:** The service core is fully documented using **OpenAPI 3.0 / Swagger UI**, providing an interactive interface to explore and test all available endpoints.
* **Stateless Communication:** All interactions are performed via RESTful principles, returning standardized JSON responses.

> **Note:** API endpoints are protected. A valid JWT token is required in the Authorization header for most operations.

### Getting Started

#### Prerequisites
* **Java SDK 21** or higher.
* **Maven 3.9+**
* **PostgreSQL 18** instance running.

#### Installation & Setup
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/youruser/mindhub-core.git](https://github.com/youruser/mindhub-core.git)
   cd mindhub-core

2. **Database Configuration:** Update src/main/resources/application.properties with your PostgreSQL credentials:
   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
    ```
3. **Build and Run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
The server will start at http://localhost:8080. You can access the Swagger UI at http://localhost:8080/swagger-ui.html to explore the API.

## License

This project is open-sourced software licensed under the MIT license. Based on official Open Source Initiative standards, this allows for personal and commercial use with attribution.
