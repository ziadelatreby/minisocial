# MiniSocial - Lightweight Social Networking App

## New Changes documentation
* [@abdelrahman's changes](#abdelrahmans-changes)

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

---
## changes documentation

* [abdelrahman](#abdelrahmans-changes)


---

### ziads changes
**current branch:**
**current version:**
**folder structure changes:**
**class' specific changes:**

---

### mostafas changes
**current branch:**
**current version:**
**folder structure changes:**
**class' specific changes:**

---

### abdelrahmans changes
**current branch:** adding-notification module
**current version:** 
**folder structure changes:**
* added rest : to contain rest api specific classes
* added entities/notification : to contain different types of notificaitons entities
* added repository : to contain dao handler (aka : database queries handlers)
* added dto : to contain the dto of entities
* added mapper : to  contain mapper between dtos and entities 
* added service : to contain services beans
* added service/notification_servie_utils : to contain different utiles needed by the notification module

**class' specific changes:**
* added the following classes list in `entities/notification`
   * which handle different types of notification , using polymorphic approach 
```
CommentNotification.java
FriendRequestNotification.java
GroupJoinNotification.java
GroupLeaveNotification.java
LikeNotification.java
//parent 
Notification.java //abstract class
```
* added `entities/GroupJoinRequest.java`, which handel group join request entity
* added `dto/NotificationDTO.java` , which handel notification dto
* added `mapper/NotificationMapper.java` mapping between entity to dto and vise versa
* added `servic/NotificationService.java` which handels complete notification sending process
* added the following list to `service/notification_servie_utiles/`
   * `MessageSender.java`
   * `NotificationListener.java`
   * 

**config notes :**
* i use jboss-eap-8
* i have added the following seciton to standalone-full.xml file :
* add this to the messaging subsystem in ur config 
  * NOTE that it can uses slightly different formate to specify the jms entities depending on SERVER VERSION
  * save a copy of the default messaging subsystem in ur config and ask chatgbt to make a suitable one
```xml
<subsystem xmlns="urn:jboss:domain:messaging-activemq:15.0">
  <server name="default">
    <!-- Define the Notifications queue -->
    <jms-queue name="NotificationsQueue">
      <entry name="java:/jms/queue/Notifications"/>
    </jms-queue>
    <!-- Dead-letter queue for poison messages -->
    <jms-queue name="DLQ">
      <entry name="java:/jms/queue/DLQ"/>
    </jms-queue>

    <!-- Address settings to move failed messages to DLQ after 5 attempts -->
    <address-setting name="#">
      <dead-letter-address>jms.queue.DLQ</dead-letter-address>
      <expiry-address>jms.queue.ExpiryQueue</expiry-address>
      <max-size-bytes>10485760</max-size-bytes>
      <max-delivery-attempts>5</max-delivery-attempts>
      <message-counter-history-day-limit>10</message-counter-history-day-limit>
    </address-setting>

    <!-- Optional: pooled connection factory to optimize JMSContext creation -->
    <pooled-connection-factory name="activemq-ra" entries="java:/JmsXA java:jboss/DefaultJMSConnectionFactory"
                               connectors="in-vm" transaction="xa" pooling-enabled="true" min-pool-size="5" max-pool-size="50"/>
  </server>
</subsystem>
```