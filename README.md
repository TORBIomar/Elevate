# Elevate Recruitment Platform 🚀

Elevate is a comprehensive full-stack recruitment platform designed to streamline the hiring process. This repository contains both the frontend and backend components of the system.

## 📂 Project Structure

- **[Elevate Front-End](./Elevate%20Front-End)**: React 19 application built with Vite and Tailwind CSS.
- **[Elevate Back-End](./Elevate%20Back-End)**: Java Spring Boot application (Maven) providing the REST API and data persistence.
- **[elevate_db_setup.sql](./elevate_db_setup.sql)**: Database schema and initial setup script.

## ✨ Core Features

### 👤 Candidate Experience
- Browse job opportunities on the **Job Board**.
- Manage professional profile and resume.
- Track application status and interviews.

### 💼 Recruiter Tools
- **Dashboard** with recruitment metrics.
- Manage job offers and candidate pipelines.
- Schedule and manage interviews.

## 🚀 Getting Started

### Backend Setup
1. Navigate to `Elevate Back-End/Elevate`.
2. Configure your database settings in `src/main/resources/application.properties`.
3. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Navigate to `Elevate Front-End`.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```

---

Built with ❤️ by [TORBIomar](https://github.com/TORBIomar)
