# 🚆 Command Line Ticket Booking System (Core Java)

## 🔹 Overview
This project is a **command-line based Ticket Booking System** developed using **Core Java** as a backend-focused application.  
It simulates a basic railway booking workflow such as **user registration, train lookup, seat availability, and ticket booking**, without using any UI framework or web layer.

The main goal of this project is to demonstrate **clean object-oriented design**, **service-layer separation**, and **local data persistence** using JSON files.

---

## 🔹 Tech Stack
- **Core Java**
- **Gradle** (Build Tool)
- **JSON Files as Local Database**
- **Command Line Interface (CLI)**

---

## 🔹 Project Architecture
The application follows a **layered structure**:


- **Entity Layer**: Holds domain objects
- **Service Layer**: Contains business logic
- **Utility Layer**: Common reusable logic
- **Storage Layer**: JSON files acting as local DB

---

## 🔹 Folder Structure

TicketBooking
│
├── src/main/java
│ ├── entity
│ │ ├── User.java
│ │ ├── Train.java
│ │ └── Ticket.java
│ │
│ ├── service
│ │ ├── UserService.java
│ │ └── TrainService.java
│ │
│ ├── util
│ │ └── UserServiceUtil.java
│ │
│ └── Main.java
│
├── src/main/resources
│ ├── user.json
│ └── train.json
│
├── build.gradle
└── README.md


---

## 🔹 Core Entities
### User
- Stores user registration and login details

### Train
- Contains train route, seat availability, and identification details

### Ticket
- Represents a booked ticket mapped to a user and train

---

## 🔹 Services
### UserService
- Handles user registration
- Performs login authentication
- Reads and writes user data to `user.json`

### TrainService
- Loads train data from `train.json`
- Searches trains by source and destination
- Manages seat availability
- Updates data after booking

### UserServiceUtil
- Common helper methods
- Input validation
- JSON read/write operations
- Shared logic used by services

---

## 🔹 Local Data Storage
This project uses **JSON files instead of a database**:
- `user.json` → Stores registered users
- `train.json` → Stores train information

These files behave as a lightweight local database and make the project easy to run without external dependencies.

---

## 🔹 How to Run the Project
### Build
```bash
gradle build
gradle run
or
java -cp build/classes/java/main Main

