#!/bin/bash

# Enhanced test script for Connection Management features in MiniSocial API
# This script tests all functionality including search filters and edge cases

API_URL="http://localhost:8080/minisocial-api/api"
ALICE_TOKEN=""
BOB_TOKEN=""
CHARLIE_TOKEN=""

echo "========== MiniSocial Connection Management - Comprehensive Test =========="

# Reset state by undeploying and redeploying the application
echo -e "\n=> [0] Redeploying application to ensure clean state..."
cd $(dirname "$0") 
mvn clean package wildfly:deploy

# 1. Register users (3 users for more comprehensive testing)
echo -e "\n=> [1] Registering test users..."
curl -s -X POST "$API_URL/users/signup" -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password123","name":"Alice Smith","bio":"First test user","role":"member"}'
curl -s -X POST "$API_URL/users/signup" -H "Content-Type: application/json" \
  -d '{"email":"bob@example.com","password":"password123","name":"Bob Jones","bio":"Second test user","role":"member"}'
curl -s -X POST "$API_URL/users/signup" -H "Content-Type: application/json" \
  -d '{"email":"charlie@example.com","password":"password123","name":"Charlie Brown","bio":"Third test user","role":"member"}'
echo "Users registered"

# 2. Sign in with users to get tokens
echo -e "\n=> [2] Signing in to get JWT tokens..."
ALICE_TOKEN=$(curl -s -X POST "$API_URL/users/signin" -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Alice token obtained: ${ALICE_TOKEN:0:20}..."

BOB_TOKEN=$(curl -s -X POST "$API_URL/users/signin" -H "Content-Type: application/json" \
  -d '{"email":"bob@example.com","password":"password123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Bob token obtained: ${BOB_TOKEN:0:20}..."

CHARLIE_TOKEN=$(curl -s -X POST "$API_URL/users/signin" -H "Content-Type: application/json" \
  -d '{"email":"charlie@example.com","password":"password123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Charlie token obtained: ${CHARLIE_TOKEN:0:20}..."

# 3. Test sending friend requests in multiple directions
echo -e "\n=> [3] Testing friend request creation..."

# Alice sends request to Bob
echo -e "\n[3.1] Alice sends friend request to Bob..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"fromUserId":1,"toUserId":2}')
echo "Result: $RESULT"

# Alice sends request to Charlie
echo -e "\n[3.2] Alice sends friend request to Charlie..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"fromUserId":1,"toUserId":3}')
echo "Result: $RESULT"

# 4. Test error handling scenarios
echo -e "\n=> [4] Testing error handling scenarios..."

# Try to send duplicate request
echo -e "\n[4.1] Alice tries to send duplicate request to Bob..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"fromUserId":1,"toUserId":2}')
echo "Result (should show conflict error): $RESULT"

# Try to send request from another user's account (should be forbidden)
echo -e "\n[4.2] Alice tries to impersonate Bob..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"fromUserId":2,"toUserId":3}')
echo "Result (should show forbidden error): $RESULT"

# 5. View pending friend requests
echo -e "\n=> [5] Viewing pending friend requests..."

echo -e "\n[5.1] Bob checks pending friend requests..."
RESULT=$(curl -s "$API_URL/connections/user/2/friend-requests" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

echo -e "\n[5.2] Charlie checks pending friend requests..."
RESULT=$(curl -s "$API_URL/connections/user/3/friend-requests" \
  -H "Authorization: Bearer $CHARLIE_TOKEN")
echo "Result: $RESULT"

# Try to view another user's friend requests (should be forbidden)
echo -e "\n[5.3] Bob tries to view Charlie's friend requests..."
RESULT=$(curl -s "$API_URL/connections/user/3/friend-requests" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result (should show forbidden): $RESULT"

# 6. Accept/Reject friend requests
echo -e "\n=> [6] Handling friend requests..."

# Bob accepts Alice's request
echo -e "\n[6.1] Bob accepts Alice's friend request..."
RESULT=$(curl -s -X PATCH "$API_URL/connections/friend-requests/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -d '{"action":"accept"}')
echo "Result: $RESULT"

# Charlie rejects Alice's request
echo -e "\n[6.2] Charlie rejects Alice's friend request..."
RESULT=$(curl -s -X PATCH "$API_URL/connections/friend-requests/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CHARLIE_TOKEN" \
  -d '{"action":"reject"}')
echo "Result: $RESULT"

# 7. View friends lists
echo -e "\n=> [7] Viewing friends lists..."

# Check Alice's friends
echo -e "\n[7.1] Alice checks her friends list..."
RESULT=$(curl -s "$API_URL/connections/user/1/friends" \
  -H "Authorization: Bearer $ALICE_TOKEN")
echo "Result: $RESULT"

# Check Bob's friends
echo -e "\n[7.2] Bob checks his friends list..."
RESULT=$(curl -s "$API_URL/connections/user/2/friends" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

# Charlie should have no friends after rejecting Alice
echo -e "\n[7.3] Charlie checks his empty friends list..."
RESULT=$(curl -s "$API_URL/connections/user/3/friends" \
  -H "Authorization: Bearer $CHARLIE_TOKEN")
echo "Result (should be empty array): $RESULT"

# 8. Test search and filter functionality
echo -e "\n=> [8] Testing search and filter functionality..."

# Create one more friend relationship for testing
echo -e "\n[8.1] Bob sends friend request to Charlie..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -d '{"fromUserId":2,"toUserId":3}')
echo "Result: $RESULT"

echo -e "\n[8.2] Charlie accepts Bob's friend request..."
RESULT=$(curl -s -X PATCH "$API_URL/connections/friend-requests/2" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CHARLIE_TOKEN" \
  -d '{"action":"accept"}')
echo "Result: $RESULT"

# Filter Bob's friends by name
echo -e "\n[8.3] Bob filters his friends by name 'Charlie'..."
RESULT=$(curl -s "$API_URL/connections/user/2/friends?name=Charlie" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result (should only show Charlie): $RESULT"

# Filter Bob's friends by partial email
echo -e "\n[8.4] Bob filters his friends by email containing 'alice'..."
RESULT=$(curl -s "$API_URL/connections/user/2/friends?email=alice" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result (should only show Alice): $RESULT"

# Use both filters together
echo -e "\n[8.5] Bob filters with both name and email (should find no one)..."
RESULT=$(curl -s "$API_URL/connections/user/2/friends?name=Charlie&email=alice" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result (should be empty array): $RESULT"

# 9. Test profile viewing
echo -e "\n=> [9] Testing profile viewing..."

# Check Alice's profile
echo -e "\n[9.1] Viewing Alice's profile (should show 1 friend)..."
RESULT=$(curl -s "$API_URL/connections/user/1/profile" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

# Check Bob's profile
echo -e "\n[9.2] Viewing Bob's profile (should show 2 friends)..."
RESULT=$(curl -s "$API_URL/connections/user/2/profile" \
  -H "Authorization: Bearer $ALICE_TOKEN")
echo "Result: $RESULT"

# Check Charlie's profile
echo -e "\n[9.3] Viewing Charlie's profile (should show 1 friend)..."
RESULT=$(curl -s "$API_URL/connections/user/3/profile" \
  -H "Authorization: Bearer $ALICE_TOKEN")
echo "Result: $RESULT"

# 10. Confirm non-existent user error handling
echo -e "\n=> [10] Testing error handling for non-existent users..."
RESULT=$(curl -s "$API_URL/connections/user/999/profile" \
  -H "Authorization: Bearer $ALICE_TOKEN")
echo "Result (should show user not found): $RESULT"

echo -e "\n========== Comprehensive Test Complete =========="
echo "Success criteria: Check that all test sections returned expected results"
echo "Tests for search/filter functionality are in section [8]"
echo "All error handling tests are in sections [4], [5.3], and [10]"

# Add tests for Post, Like, and Comment functionality
echo -e "\n=> [11] Testing Post, Like, and Comment functionality..."

# Create a post from Alice
echo -e "\n[11.1] Alice creates a post..."
ALICE_POST_RESULT=$(curl -s -X POST "$API_URL/posts" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"content":"This is Alices first post!","imageUrl":"https://example.com/image.jpg"}' \
)
echo "Alices post created: $ALICE_POST_RESULT"
ALICE_POST_ID=$(echo $ALICE_POST_RESULT | grep -o '"id":[0-9]*' | head -1 | sed 's/"id"://')

# Create a post from Bob
echo -e "\n[11.2] Bob creates a post..."
BOB_POST_RESULT=$(curl -s -X POST "$API_URL/posts" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -d '{"content":"Bobs awesome post!"}' \
)
echo "Bobs post created: $BOB_POST_RESULT"
BOB_POST_ID=$(echo $BOB_POST_RESULT | grep -o '"id":[0-9]*' | head -1 | sed 's/"id"://')

# Like posts
echo -e "\n[11.3] Bob likes Alices post..."
LIKE_RESULT=$(curl -s -X PATCH "$API_URL/posts/$ALICE_POST_ID/likes" \
  -H "Authorization: Bearer $BOB_TOKEN" \
)
echo "Like result: $LIKE_RESULT"

# Check post with likes
echo -e "\n[11.4] View Alices post with like count..."
POST_VIEW_RESULT=$(curl -s -X GET "$API_URL/posts/$ALICE_POST_ID" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
)
echo "Post view result: $POST_VIEW_RESULT"

# Comment on posts
echo -e "\n[11.5] Charlie comments on Alices post..."
COMMENT_RESULT=$(curl -s -X POST "$API_URL/posts/$ALICE_POST_ID/comments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CHARLIE_TOKEN" \
  -d '{"content":"Great post, Alice!"}' \
)
echo "Comment result: $COMMENT_RESULT"
COMMENT_ID=$(echo $COMMENT_RESULT | grep -o '"id":[0-9]*' | head -1 | sed 's/"id"://')

# View comments
echo -e "\n[11.6] View comments on Alices post..."
COMMENTS_RESULT=$(curl -s -X GET "$API_URL/posts/$ALICE_POST_ID/comments" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
)
echo "Comments view result: $COMMENTS_RESULT"

# Get user feed
echo -e "\n[11.7] Alice views her feed..."
FEED_RESULT=$(curl -s -X GET "$API_URL/posts/feed/1" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
)
echo "Feed result: $FEED_RESULT"

echo -e "\n========== Post, Like, and Comment Tests Complete ==========="

# TODO: 
#   - add search (view) by name
#   - add search (view) by email
#   - remove view by id?