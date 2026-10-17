# 🚀 Elevate — Next-Generation Recruitment & ATS Platform

<div align="center">

[![React 19](https://img.shields.io/badge/Frontend-React%2019%20%7C%20Vite-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Tailwind CSS](https://img.shields.io/badge/Styling-Tailwind%20CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)
[![MySQL](https://img.shields.io/badge/Database-MySQL%208-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

<p align="center">
  <b>Comprehensive full-stack recruitment platform and applicant tracking system (ATS).</b><br>
  Bridges talent with hiring managers through an intuitive application pipeline, automated candidate scoring, and stage tracking.
</p>

</div>

---

## 🌟 Key Features

### 👤 Candidate Portal
- **Job Discovery**: Explore active job openings with tag filters, salary ranges, and remote options.
- **Profile & Resume Builder**: Manage professional credentials, portfolio links, and uploaded CVs.
- **Application Tracking**: Live status updates from application submission to interview offer.

### 🏢 Recruiter Dashboard
- **Kanban Pipeline**: Drag-and-drop candidates across stages (*Applied*, *Screening*, *Interview*, *Offer*, *Rejected*).
- **Candidate Evaluation**: Score applicants, leave collaborative hiring notes, and schedule interview rounds.
- **Database Architecture**: Clean normalized schema with initial seed script (`elevate_db_setup.sql`).

---

## 📂 Project Structure

- **`Elevate Front-End/`**: React 19 single-page app built with Vite and Tailwind CSS.
- **`Elevate Back-End/`**: Java Spring Boot application providing the RESTful API and security layer.
- **`elevate_db_setup.sql`**: Production database schema and initial seed data.

---

## 🚀 Getting Started

### 1. Database Setup
```bash
mysql -u root -p < elevate_db_setup.sql
```

### 2. Backend API
```bash
cd "Elevate Back-End"
mvn clean install
mvn spring-boot:run
```

### 3. Frontend App
```bash
cd "Elevate Front-End"
npm install
npm run dev
```

Access the application at `http://localhost:5173`.

---

## 👨‍💻 Author

**Omar Torbi** — Full-Stack Engineer & Software Architect
- 🌐 **Portfolio**: [omartorbi.engineer](https://www.omartorbi.engineer)
- 💼 **LinkedIn**: [linkedin.com/in/omar-torbi](https://www.linkedin.com/in/omar-torbi-b8340933a/)
- 🐙 **GitHub**: [@TORBIomar](https://github.com/TORBIomar)
- ✉️ **Email**: [torbi.dev@outlook.com](mailto:torbi.dev@outlook.com)
