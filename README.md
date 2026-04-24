# Guardrail System

A robust, high-performance Spring Boot microservice that acts as the central API gateway and guardrail system. This system handles concurrent requests, manages distributed state using Redis, and implements event-driven scheduling to prevent AI compute runaway through strict mathematical guardrails.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **PostgreSQL** (Database)
- **Redis** (Distributed State Management)
- **Spring Data JPA/Hibernate** (ORM)
- **Spring Data Redis** (Redis Integration)
- **Docker & Docker Compose** (Containerization)

## Prerequisites

- Docker and Docker Compose installed
- Java 17+ (for local development)
- Maven (for local development)

## Getting Started

1. **Start the services:**
   ```bash
   docker-compose up -d
   ```

2. **Access the application:**
   - The Spring Boot application will start on port 8080
   - PostgreSQL is available on port 5433 (mapped from container port 5432)
   - Redis is available on port 6379

3. **Verify services are running:**
   - PostgreSQL: `docker ps` should show `postgres_db_guardrail`
   - Redis: `docker ps` should show `redis_cache`

## Database Schema

The system uses JPA/Hibernate with the following entities:

- **User**: `id`, `username`, `is_premium`
- **Bot**: `id`, `name`, `persona_description`
- **Post**: `id`, `author_id` (references User or Bot), `content`, `created_at`
- **Comment**: `id`, `post_id`, `author_id`, `content`, `depth_level`, `created_at`
- **PostLike**: `id`, `post_id`, `user_id`, `created_at`

## API Endpoints

### Posts
- `POST /api/posts` - Create a new post
  ```json
  {
    "authorId": "user_or_bot_id",
    "content": "Post content"
  }
  ```

- `POST /api/posts/{postId}/comments` - Add a comment to a post
  ```json
  {
    "authorId": "user_or_bot_id",
    "content": "Comment content"
  }
  ```

- `POST /api/posts/{postId}/like` - Like a post
  ```json
  {
    "userId": "user_id"
  }
  ```

## Implementation Details

### Phase 1: Core API & Database Setup
- Spring Boot application with PostgreSQL integration
- JPA entities with proper relationships
- RESTful API endpoints for basic CRUD operations

### Phase 2: The Redis Virality Engine & Atomic Locks

#### Virality Score Calculation
Real-time virality scoring using Redis:
- Bot Reply: +1 point
- Human Like: +20 points
- Human Comment: +50 points

Scores are stored in Redis keys: `post:{id}:virality_score`

#### Atomic Locks (Concurrency Protection)
Three guardrails implemented using Redis atomic operations:

1. **Horizontal Cap**: Maximum 100 bot replies per post
   - Redis key: `post:{id}:bot_count`
   - Uses `INCR` operation for atomic increment

2. **Vertical Cap**: Maximum 20 levels deep for comment threads
   - Checked before allowing new comments

3. **Cooldown Cap**: Bot can interact with specific human only once per 10 minutes
   - Redis key: `cooldown:bot_{id}:human_{id}` with 10-minute TTL

### Phase 3: The Notification Engine (Smart Batching)

#### Redis Throttler
- Checks if user received notification in last 15 minutes
- If yes: Queues notification in `user:{id}:pending_notifs` list
- If no: Sends immediate notification and sets 15-minute cooldown

#### CRON Sweeper
- `@Scheduled` task runs every 5 minutes (configurable for testing)
- Processes all users with pending notifications
- Summarizes multiple notifications: "Bot X and [N] others interacted with your posts"
- Clears Redis lists after processing

### Phase 4: Corner Cases & Testing Criteria

#### Race Conditions Protection
- 200 concurrent bot comment requests handled atomically
- Redis ensures exactly 100 comments maximum (no 101st comment)

#### Statelessness
- All state management in Redis (no Java memory storage)
- Counters, cooldowns, and notifications stored in Redis

#### Data Integrity
- PostgreSQL as source of truth for content
- Redis as gatekeeper for business rules
- Database transactions only committed after Redis validation

## Thread Safety Implementation

Thread safety for Atomic Locks is guaranteed through Redis atomic operations:

1. **Atomic Increment Operations**: Using Redis `INCR` command for counters (bot_count, virality_score)
2. **Atomic Existence Checks**: Using `EXISTS` to check cooldown keys
3. **TTL-based Expiration**: Automatic cleanup of cooldown keys using Redis TTL
4. **Pipeline Operations**: Grouping multiple Redis operations in atomic transactions where needed

Redis ensures that all operations are atomic at the single-command level, preventing race conditions even under high concurrency (200+ simultaneous requests).

## Testing

The system is designed to handle:
- 200 concurrent bot comment requests
- Race condition prevention
- Data integrity under load
- Stateless operation

## Deliverables

1. **Source Code**: Complete Spring Boot application
2. **Docker Compose**: `docker-compose.yml` for PostgreSQL and Redis
3. **Postman Collection**: Available in repository for API testing
4. **This README**: Implementation details and usage guide

## Architecture



## Development

For local development:
1. Start Docker services: `docker-compose up -d`
2. Run Spring Boot: `mvn spring-boot:run`
3. Application starts on `http://localhost:8080`

## Configuration

Database and Redis configurations are in `application.properties`:
- PostgreSQL: localhost:5433
- Redis: localhost:6379 
