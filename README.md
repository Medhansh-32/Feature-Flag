# Feature-Flag
# Feature Flag Service

## Overview

The Feature Flag Service is a gRPC-based microservice built with Spring Boot that dynamically enables or disables features for users at runtime.

It allows controlled feature rollouts without redeploying applications. Feature availability is determined using configurable business rules such as user role, plan, location, app version, and rollout percentage.

This service is designed to be scalable, deterministic, and production-ready for distributed environments.

---

## What This Service Does

For a given user and feature name, the service:

1. Retrieves the user from the database
2. Fetches feature configuration (cached)
3. Evaluates feature rules:
   - Feature active status
   - Pin code restrictions
   - Role restrictions
   - Plan restrictions
   - App version rules
   - Percentage rollout
4. Returns a boolean response indicating whether the feature is enabled

The service follows a fail-safe approach: if any error occurs, the feature is returned as disabled.

---

## Key Capabilities

- Rule-based feature enablement
- Deterministic percentage rollout
- Version-based feature gating
- Role and plan targeting
- Location (pin code) targeting
- gRPC communication for high performance
- Cache-backed feature lookup
- Stateless design for horizontal scaling

---

## How Rollout Works

Rollout percentage is calculated using a deterministic hashing strategy:

- A hash is generated from `userId + featureName`
- Result is bucketed into 0–99
- Feature is enabled if bucket value is less than rollout percentage

This ensures:
- Consistent results per user
- Gradual feature rollouts (10%, 25%, 50%, etc.)
- Safe production experimentation

---

## Technology Stack

- Java 21
- Spring Boot
- gRPC
- Protobuf
- Maven
- JPA / Hibernate
- Caching (e.g., Redis or in-memory cache)
- Docker

---

## Architecture Characteristics

- Stateless microservice
- Database-backed feature configuration
- Cache-first feature retrieval
- Deterministic evaluation logic
- Designed for Kubernetes or containerized deployments

---

## Communication Protocol

The service exposes a gRPC endpoint.

Request:
- User ID
- Feature Name

Response:
- `enabled: true | false`

---

## Intended Use Cases

- Gradual feature rollout
- A/B testing foundation
- Environment-based feature control
- Role-based feature gating
- Safe production releases
- Mobile app version control

---

## Deployment

The service can be:

- Run locally using Maven
- Packaged as a Docker container
- Deployed to Kubernetes
- Integrated into CI/CD pipelines

---

## Design Principles

- Fail-safe by default (disabled on failure)
- Deterministic behavior
- High performance via gRPC
- Clean separation of concerns
- Extensible rule evaluation logic

---

## Future Enhancements (Optional)

- Admin dashboard for feature management
- Metrics & observability integration
- Multi-variant experimentation
- Distributed caching improvements
- Feature audit logging

---

## License

Internal / Private Repository
