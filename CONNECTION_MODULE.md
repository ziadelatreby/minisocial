# Connection Management Module

This module implements user connection management functionality for the MiniSocial application including friend requests and friend relationships.

## Features

- Send friend requests
- Accept/reject friend requests
- View pending friend requests
- View friends list with filtering options
- View user profiles with friend counts

## API Endpoints

### Friend Request Management

- `POST /connections/friend-requests`
  - Send a friend request from one user to another
  - Required body: `{ "fromUserId": 1, "toUserId": 2 }`
  - Authentication: Required (JWT token)
  - The fromUserId must match the authenticated user's ID

- `GET /connections/user/{user-id}/friend-requests`
  - Get pending friend requests for a specific user
  - Authentication: Required (JWT token)
  - User can only view their own friend requests

- `PATCH /connections/friend-requests/{from-user-id}`
  - Accept or reject a friend request
  - Required body: `{ "action": "accept" }` or `{ "action": "reject" }`
  - Authentication: Required (JWT token)

### Friendship Management

- `GET /connections/user/{user-id}/friends`
  - Get a user's friends list
  - Optional query parameters: `name` (filter by name), `email` (filter by email)
  - Authentication: Required (JWT token)

- `GET /connections/user/{user-id}/profile`
  - Get user profile including friend count
  - Authentication: Required (JWT token)

## Implementation Details

### Database Structure

- `User` entity has a many-to-many self-referential relationship for friends
- `user_friends` join table with columns: `user_id` and `friend_id`
- `FriendRequest` entity tracks friend requests with statuses: pending, accepted, rejected

### Key Components

- `ConnectionResource`: REST endpoints for connection management
- `FriendRequestService`: Business logic for friend requests and friendship
- `UserRepository`: Data access for user and friendship management
- `FriendRequestRepository`: Data access for friend request management

### DTOs

- `FriendRequestDTO`: For sending friend requests
- `FriendRequestResponseDTO`: For viewing friend requests
- `FriendRequestActionDTO`: For accepting/rejecting requests
- `UserProfileDTO`: For user profile display with friend count

## Testing

- A Postman collection is provided: `minisocial_connection_module.postman_collection.json`
- A bash test script is available: `test_connections.sh`

## Implementation Notes

- Direct SQL queries are used to avoid Hibernate lazy loading issues
- Friend counts are calculated using direct database queries
- Bidirectional friendship relationships are maintained consistently
