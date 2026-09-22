# 🎓 Student Management System (Java + Spring Boot)

A full-stack Java web app for managing student records — register, log in, and
edit your own profile (Name, Section, GPA, Email) — with server-side
validation and a real persistent database.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3, Spring MVC, Spring Data JPA
- **Database:** H2 (file-based, embedded — zero setup, or swap to MySQL, see below)
- **Frontend:** Thymeleaf + Bootstrap 5 + custom CSS (gradient/glassmorphism UI)
- **Security:** BCrypt password hashing, session-based login guard

## Features

- Register a student account
- Field validation with clear inline error messages:
  - **Name** — each word must start with a capital letter (e.g. `Rahul Sharma`)
  - **Section** — exactly one capital letter + one digit (e.g. `A1`, `B3`)
  - **GPA** — number between `0.0` and `10.0`, max 2 decimal places
  - **Email** — must contain `@` and a valid domain, and must be unique
  - **Password** — minimum 6 characters, stored hashed (BCrypt), never in plain text
- Login with email + password (session-based)
- Dashboard showing your saved profile
- Edit profile screen to update any field (password change optional)
- Data is persisted to a real database file (`./data/studentdb.mv.db`) so it
  survives restarts

## Project Structure

```
student-management-system/
├── pom.xml
├── src/main/java/com/studentms/
│   ├── StudentManagementApplication.java
│   ├── model/          # Student, LoginForm, EditProfileForm
│   ├── repository/     # StudentRepository (Spring Data JPA)
│   ├── service/        # StudentService (register/login/update logic)
│   ├── controller/     # AuthController, DashboardController
│   └── config/         # Session-based login guard
└── src/main/resources/
    ├── application.properties
    ├── templates/       # login.html, register.html, dashboard.html, edit.html
    └── static/css/style.css
```

## How to Run

### Prerequisites
- Java 17 or newer ([download](https://adoptium.net/))
- Maven ([download](https://maven.apache.org/download.cgi)) — or use an IDE
  like IntelliJ IDEA / Eclipse / VS Code which bundles Maven support

### Option 1 — Command line
```bash
cd student-management-system
mvn spring-boot:run
```

### Option 2 — Build and run a jar
```bash
mvn clean package
java -jar target/student-management-system-1.0.0.jar
```

Then open **http://localhost:8080** in your browser.

No database installation is required — it uses an embedded H2 database that
saves to a local file automatically. You can browse the raw data at
**http://localhost:8080/h2-console** (JDBC URL:
`jdbc:h2:file:./data/studentdb`, user: `sa`, no password).

### Switching to MySQL (optional)

If you'd rather use MySQL, open `src/main/resources/application.properties`,
comment out the H2 lines, uncomment the MySQL block at the bottom, and add
this dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Notes on Validation

All validation happens **server-side** using Jakarta Bean Validation
annotations (`@Pattern`, `@Email`, `@DecimalMin/Max`, etc.) on the model
classes, so it can't be bypassed by disabling JavaScript. Errors are shown
next to the relevant field on the form.

## Ideas to Extend This Project

- Add an admin role that can view/manage *all* students
- Add profile pictures (file upload)
- Add "forgot password" via email
- Add pagination/search if you extend it to list all students
- Write unit tests for `StudentService` (registration, login, update logic)
- Containerize with Docker

## Posting This on LinkedIn 💡

A few tips that tend to work well:
1. Take a few screenshots — the login page, the register page (showing a
   validation error), and the dashboard — and post them as a carousel/image.
2. Briefly explain what you built and the tech stack (mention Java, Spring
   Boot, JPA, database, validation, and the login system).
3. Mention one specific technical decision you made (e.g. "used BCrypt to
   hash passwords instead of storing them in plain text" or "used regex
   validation for structured fields like Section").
4. Link to the GitHub repo if you push this project there, and use hashtags
   like `#Java #SpringBoot #WebDevelopment #StudentProject`.
