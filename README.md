# JobBoard — Spring Fundamentals Exam Project

A small job board where **employers** create companies and post jobs, and **applicants** apply with a cover letter. Built for the Spring Fundamentals — Regular Exam (May 2026).

## Tech stack

- Java 17
- Spring Boot 3.4.0 (Web, Data JPA, Validation, Thymeleaf)
- Spring Security Crypto (BCrypt only — no Spring Security filters; session auth is custom)
- MySQL 8 (relational DB, UUID primary keys)
- Maven
- Thymeleaf templates

## Architecture

Single Spring Boot application running on port **8080**, server-rendered with Thymeleaf.

```
com.example.jobboard
├── JobBoardApplication
├── config        (PasswordConfig, WebConfig + AuthInterceptor)
├── model         (User, Company, JobPost, Application + enums)
├── repository    (Spring Data JPA repositories, one per entity)
├── service       (UserService, CompanyService, JobPostService, ApplicationService)
├── dto           (Register, Login, Company, JobPost, Application DTOs with Jakarta Validation)
├── exception     (NotFoundException, ForbiddenException, BusinessException, GlobalExceptionHandler)
└── web           (HomeController, AuthController, CompanyController, JobPostController, ApplicationController, SessionUser, AuthInterceptor)
```

## Domain entities (3, as required)

1. **Company** — owned by an employer (User). Has name, description, website.
2. **JobPost** — belongs to a Company. Has title, description, location, salary, createdOn.
3. **Application** — links an applicant (User) to a JobPost with cover letter + status.

`User` is a supporting/auth entity (not counted toward the 3 domain entities). All primary keys are **UUID**. At least one relationship: `JobPost @ManyToOne Company`, `Application @ManyToOne JobPost`, `Company @ManyToOne User`.

## Functionalities (4 valid, each full CRUD, not on the User entity)

| # | Functionality                  | Trigger                                | Endpoints |
| - | ------------------------------ | -------------------------------------- | --------- |
| 1 | Manage companies (employer)    | Forms in `/companies` views            | `POST /companies`, `POST /companies/{id}`, `POST /companies/{id}/delete` |
| 2 | Manage job postings (employer) | Forms in `/jobs/manage` & `/jobs/new`  | `POST /jobs`, `POST /jobs/{id}`, `POST /jobs/{id}/delete` |
| 3 | Apply to a job (applicant)     | `/applications/apply/{jobId}` form     | `POST /applications/apply/{jobId}`, `POST /applications/{id}` (edit cover letter), `POST /applications/{id}/withdraw` |
| 4 | Decide on application (employer) | Accept/Reject buttons in `/applications/received` | `POST /applications/{id}/decide` (status=ACCEPTED|REJECTED) |

All show a visible UI result (redirect to a refreshed list / detail).

## Web pages (≥6, ≥4 dynamic, 1 static)

1. `/` — home with latest postings (dynamic)
2. `/about` — static informational page
3. `/register` — registration form (dynamic)
4. `/login` — login form (dynamic)
5. `/jobs` — list of all jobs (dynamic)
6. `/jobs/view/{id}` — job detail (dynamic)
7. `/jobs/manage`, `/jobs/new`, `/jobs/{id}/edit` — employer CRUD (dynamic)
8. `/companies`, `/companies/new`, `/companies/{id}/edit` — employer CRUD (dynamic)
9. `/applications/mine`, `/applications/received`, `/applications/apply/{jobId}`, `/applications/{id}/edit` — application flows (dynamic)

## Security

- Session-based login. `user_id` (wrapped in `SessionUser`) is stored in `HttpSession` under key `SESSION_USER`.
- Passwords are **BCrypt-hashed** (`PasswordEncoder` bean).
- A `HandlerInterceptor` (`AuthInterceptor`) blocks unauthenticated access to everything except: `/`, `/about`, `/login`, `/register`, `/logout`, `/jobs`, `/jobs/view/**`, static assets, error pages.
- Role checks inside controllers/services:
  - Only `EMPLOYER` can create/edit/delete companies and job postings, or decide on applications.
  - Only `APPLICANT` can apply to or withdraw an application.
  - Owner check: a user can only edit/delete their own resources (`ForbiddenException` otherwise).

## Data validation & error handling

- All form DTOs use `@Valid` with Jakarta Validation (`@NotBlank`, `@Size`, `@Email`, `@DecimalMin/Max`, `@NotNull`).
- On validation failure, the form is redisplayed with field-level red error messages.
- Custom exceptions (`NotFoundException`, `ForbiddenException`, `BusinessException`) are caught by `GlobalExceptionHandler` and rendered as `error/404.html`, `error/403.html`, `error/400.html`.
- Business rules: unique username/email, password confirmation match, no duplicate applications, no applying to own job, no editing application after decision.

## Running

1. Start MySQL locally (default `root` / `root`, or edit `application.properties`).
2. `mvn spring-boot:run`
3. Open http://localhost:8080

The schema is created automatically via `spring.jpa.hibernate.ddl-auto=update`.

## Integrations

None beyond the relational DB. The project is self-contained.
