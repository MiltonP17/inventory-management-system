# Inventory Management System

A full-stack inventory management application built with Java, Spring Boot, Thymeleaf, Bootstrap, Spring Data JPA, and H2. The application helps a small supply business manage parts, products, inventory levels, and basic purchase workflows from a web-based interface.

## Overview

This project models a small business inventory system for **Patch & Switch Supply Co.**, a fictional supplier of network closet parts and installation kits. Users can view, search, add, update, and delete parts and products. The application also includes business rules for inventory limits and purchasing behavior.

## Features

- View and search current parts and products
- Add, update, and delete in-house and outsourced parts
- Add, update, and delete products
- Associate parts with products
- Purchase products with inventory reduction logic
- Display success and failure messages for purchase attempts
- Enforce minimum and maximum inventory rules
- Persist inventory data using H2 database storage
- Seed sample inventory data on startup
- Unit tests for inventory-related model behavior

## Stack

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Thymeleaf
- Bootstrap
- H2 Database
- Maven
- JUnit

## Project Structure

```text
src/main/java/com/example/demo
├── bootstrap       # Sample data loaded at application startup
├── controllers     # Web request handling and page routing
├── domain          # Entity/model classes
├── repositories    # Spring Data repository interfaces
├── service         # Business logic layer
└── validators      # Custom validation rules

src/main/resources
├── templates       # Thymeleaf HTML pages
├── static          # CSS and static assets
└── application.properties
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run Locally

Clone the repository:

```bash
git clone https://github.com/milt17p/inventory-management-system.git
cd inventory-management-system
```

Run the application:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080/mainscreen
```

### Run Tests

```bash
mvn test
```

## Database

The application uses an H2 database with file-based persistence. The H2 console is enabled for local development.

```text
http://localhost:8080/h2-console
```

Default database settings are located in:

```text
src/main/resources/application.properties
```

## Future Improvements

- Improve responsive layout and visual design
- Add screenshots and usage examples
- Deploy a live demo
- Add authentication

## Project Status
Core functionality is complete. Current improvements focus on UI polish, deployment, and expanded documentation.