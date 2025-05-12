#!/bin/bash

# Test script for Connection Management features in MiniSocial API

API_URL="http://localhost:8080/minisocial-api/api"
ALICE_TOKEN=""
BOB_TOKEN=""

echo "========== MiniSocial Connection Management Test =========="

# 1. Register users
echo -e "\n1. Registering users..."
curl -s -X POST "$API_URL/users/signup" -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password123","name":"Alice Smith","bio":"First test user","role":"member"}'
curl -s -X POST "$API_URL/users/signup" -H "Content-Type: application/json" \
  -d '{"email":"bob@example.com","password":"password123","name":"Bob Jones","bio":"Second test user","role":"member"}'
echo "Users registered"

# 2. Sign in with both users to get tokens
echo -e "\n2. Signing in to get JWT tokens..."
ALICE_TOKEN=$(curl -s -X POST "$API_URL/users/signin" -H "Content-Type: application/json" \
  -d '{"email":"alice@example.com","password":"password123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Alice token obtained"

BOB_TOKEN=$(curl -s -X POST "$API_URL/users/signin" -H "Content-Type: application/json" \
  -d '{"email":"bob@example.com","password":"password123"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Bob token obtained"

# 3. Test sending friend request: Alice -> Bob
echo -e "\n3. Alice sends friend request to Bob..."
RESULT=$(curl -s -X POST "$API_URL/connections/friend-requests" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ALICE_TOKEN" \
  -d '{"fromUserId":1,"toUserId":2}')
echo "Result: $RESULT"

# 4. Bob checks pending friend requests
echo -e "\n4. Bob checks pending friend requests..."
RESULT=$(curl -s "$API_URL/connections/user/2/friend-requests" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

# 5. Bob accepts Alice's friend request
echo -e "\n5. Bob accepts Alice's friend request..."
RESULT=$(curl -s -X PATCH "$API_URL/connections/friend-requests/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $BOB_TOKEN" \
  -d '{"action":"accept"}')
echo "Result: $RESULT"

# 6. Check Alice's friends list
echo -e "\n6. Checking Alice's friends list..."
RESULT=$(curl -s "$API_URL/connections/user/1/friends" \
  -H "Authorization: Bearer $ALICE_TOKEN")
echo "Result: $RESULT"

# 7. Check Bob's friends list
echo -e "\n7. Checking Bob's friends list..."
RESULT=$(curl -s "$API_URL/connections/user/2/friends" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

# 8. Check Alice's profile
echo -e "\n8. Checking Alice's profile..."
RESULT=$(curl -s "$API_URL/connections/user/1/profile" \
  -H "Authorization: Bearer $BOB_TOKEN")
echo "Result: $RESULT"

echo -e "\n========== Test Complete =========="
