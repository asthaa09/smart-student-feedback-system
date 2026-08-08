# Smart Student Feedback and Analytics System

Full-stack application with:
- `backend`: Spring Boot, Spring Security, JWT, MySQL, JPA
- `frontend`: React, Axios, Recharts

## Features

- JWT authentication with role-based authorization (`STUDENT`, `FACULTY`, `ADMIN`)
- Student feedback submission with:
  - Rating (`1-5`)
  - Comment
  - Anonymous option
- Faculty dashboard:
  - View subject feedback
  - Average rating
  - Sentiment split (positive/negative/neutral)
- Admin dashboard:
  - Subject-wise average ratings
  - Top and low-performing faculty
  - Chart-based analytics
- Basic keyword-driven sentiment analysis
- Validation and global error handling

## Backend Setup

1. Create MySQL database user credentials or update `backend/src/main/resources/application.yml`.
2. Set Java and run:

```bash
cd backend
mvn spring-boot:run
```

Default seeded users:
- Admin: `admin@college.com` / `admin123`
- Faculty: `faculty@college.com` / `faculty123`
- Student: `student@college.com` / `student123`

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173` and calls backend at `http://localhost:8080/api`.
