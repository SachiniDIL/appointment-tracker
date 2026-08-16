# Appointment Tracker

A small appointment-scheduling app built as Java/Spring Boot interview practice: a Spring Boot + Oracle backend with a React frontend.

## Backend

Spring Boot 4.1.0, Java 21, Oracle DB. Runs on `http://localhost:8080`.

```bash
cd backend
mvn spring-boot:run
```

## Frontend

React + TypeScript, built with Vite. Runs on `http://localhost:5173`.

```bash
cd frontend
npm install
npm run dev
```

The backend's `CorsConfig` allows requests from `http://localhost:5173`, so run both together for the app to work end-to-end.

### Frontend scripts

- `npm run dev` — start the dev server
- `npm test` — run the Vitest test suite
- `npm run lint` — run ESLint
- `npm run build` — type-check and build for production
