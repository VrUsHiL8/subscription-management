# 📦 Subscription Management System

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.0-brightgreen.svg)
![Angular](https://img.shields.io/badge/Angular-15+-red.svg)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue.svg)
![JWT](https://img.shields.io/badge/Security-JWT-orange.svg)

> A full-stack web application to help users manage, track, and renew their subscriptions like Netflix, Spotify, etc. Built using Angular, Spring Boot, MySQL, and JWT Authentication.

---

## 📚 Table of Contents

- [🔧 Tech Stack](#-tech-stack)
- [✨ Features](#-features)
- [📁 Project Structure](#-project-structure)
- [🚀 Getting Started](#-getting-started)
  - [🖥️ Backend Setup](#️-backend-setup)
  - [🌐 Frontend Setup](#-frontend-setup)
- [🧪 API Endpoints](#-api-endpoints)
- [🔒 Security](#-security)
- [📈 Future Enhancements](#-future-enhancements)
- [🤝 Contributing](#-contributing)
- [📝 License](#-license)

---

## 🔧 Tech Stack

| Layer      | Technology                     |
|------------|--------------------------------|
| Frontend   | Angular 15+, HTML, CSS, Angular Material |
| Backend    | Spring Boot 3, Java 17+        |
| Database   | MySQL                          |
| Security   | JWT (JSON Web Token)           |
| OAuth      | Google OAuth 2.0 *(Planned)*   |

---

## ✨ Features

- 🔐 **User Authentication** with JWT (Login/Signup)
- 📦 **Manage Subscriptions**: Add, Edit, Delete
- 🧾 **View All Subscriptions** (e.g., Netflix, Spotify)
- 🗂️ **Filter by Categories** (Entertainment, Education, etc.)
- 🔁 **Manual Renewals** with early renewal support
- 🕒 **Upcoming and Due Renewals** Dashboard
- 🌐 **OAuth Integration** (planned for third-party apps)

---

## 📁 Project Structure

subscription-management/
├── backend/ # Spring Boot backend application
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/ # Java source code (controllers, services, models)
│ │ │ └── resources/ # Application properties, configs
│ └── pom.xml # Maven configuration
├── frontend/ # Angular frontend application
│ ├── src/
│ │ ├── app/ # Angular components, services, modules
│ │ ├── assets/ # Static assets (images, styles)
│ └── angular.json # Angular CLI config
├── .gitignore
└── README.md


