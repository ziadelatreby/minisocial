# MiniSocial - Lightweight Social Networking App

**MiniSocial** is a Jakarta EE-based social networking platform designed for collaborative environments. Users can connect, share content, and engage in group interactions with built-in real-time notifications and secure role-based access control.

*This is a university project and it is for learning purposes only.*

---

## Tech Stack

* **Java 17**
* **Jakarta EE 9.1+**
* **IntelliJ IDEA Ultimate**
* **WildFly 29**
* **Maven**

**Core Components:**

* EJB (Business Logic)
* JPA (Persistence)
* JTA (Transaction Management)
* JAX-RS (REST APIs)
* JAAS (Security)
* JMS (Notifications)

---

## Features Overview

### 1. User Management

* Register, login, update profile
* Role selection (user/admin)

### 2. Connection Management

* Send/receive/accept/reject friend requests
* View friends & profiles

### 3. Post Management

* Create, edit, delete posts with images/links
* Like, comment, and view timeline feed

### 4. Group Management

* Create/join/leave groups (open/closed)
* Admin tools: approve members, manage posts/users
* Group-specific posts & roles

### 5. JMS Notifications

* Real-time alerts for friend requests, post interactions, group events
* Standardized event object via message-driven beans

### 6. Security

* JAAS-based role restrictions (Admin/User)
* API access control with 403 on violations

---

## Setup Instructions

1. **Install Java 17**
2. **Install IntelliJ IDEA Ultimate**
3. **Install & Configure Jboss/WildFly 29**
4. **Create Project:**
   * Jakarta EE (REST template, Full Platform)
   * Java 17, Maven
5. **Pull Project Code:**

   ```bash
   git fetch origin
   git reset --hard origin
   ```
6. **Run and Test**

[More details on setup](docs/setup.md)

---

## Complete project structure

---

## Postman collection for all APIs
