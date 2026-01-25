# Shoghlana 

Shoghlana is a backend system for a local services marketplace where customers can post jobs and skilled workers (plumbers, electricians, etc.) can apply for them.

## Tech Stack
- Java 21, Spring Boot
- Spring Security, JWT, OAuth2
- JPA / Hibernate
- Events
- MySQL, Redis
- Docker & Docker Compose

## Features
- Unified authentication (Email, Phone, OAuth2 {Google , Facebook})
- Role-based access (CUSTOMER, WORKER, ADMIN)
- Job posting, applications, and reviews
- Notifications and caching
- Pagination, sorting and results from Filter. 

## Architecture
- Modular Monolith
- Separate security (AppUser) from business profiles
- Flyway for database migrations

## Status
Actively developed as a production-style backend project.
