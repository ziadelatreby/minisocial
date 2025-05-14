#!/bin/bash

# Configuration (can be overridden by environment variables)
MINISOCIAL_DB_USER=${MINISOCIAL_DB_USER:-"appuser"}
MINISOCIAL_DB_PASS=${MINISOCIAL_DB_PASS:-"1452a7a"}
MINISOCIAL_DB_NAME=${MINISOCIAL_DB_NAME:-"minisocial"}

echo "Cleaning database..."
PGPASSWORD=$MINISOCIAL_DB_PASS psql -U $MINISOCIAL_DB_USER -d $MINISOCIAL_DB_NAME -c "
-- Drop all notification tables first (they depend on other tables)
DROP TABLE IF EXISTS commentnotification CASCADE;
DROP TABLE IF EXISTS friendrequestnotification CASCADE;
DROP TABLE IF EXISTS groupjoinnotification CASCADE;
DROP TABLE IF EXISTS groupleavenotification CASCADE;
DROP TABLE IF EXISTS likenotification CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;

-- Drop relationship tables
DROP TABLE IF EXISTS user_friends CASCADE;
DROP TABLE IF EXISTS friend_requests CASCADE;
DROP TABLE IF EXISTS users_groups CASCADE;
DROP TABLE IF EXISTS post_likes CASCADE;
DROP TABLE IF EXISTS group_join_requests CASCADE;

-- Drop main entity tables
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Drop sequences
DROP SEQUENCE IF EXISTS comments_id_seq CASCADE;
DROP SEQUENCE IF EXISTS friend_requests_id_seq CASCADE;
DROP SEQUENCE IF EXISTS groups_id_seq CASCADE;
DROP SEQUENCE IF EXISTS notifications_id_seq CASCADE;
DROP SEQUENCE IF EXISTS posts_id_seq CASCADE;
DROP SEQUENCE IF EXISTS users_groups_id_seq CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
"

echo "Building and deploying project..."
mvn clean package wildfly:deploy

echo "Done!"
