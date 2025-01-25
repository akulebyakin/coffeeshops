# CoffeeShops Web Application

CoffeeShops is a web-based application that allows users to manage coffee shops and their details. It includes user authentication, role-based access, and the ability to edit user profiles and coffee shop information. Users can also upload and manage profile avatars.

---

## Features

- **User Management**:
    - Registration and authentication.
    - Role-based access (ADMIN, MANAGER, USER).
    - Edit user profiles, including avatars.

- **Coffee Shop Management**:
    - Add, edit, and delete coffee shops.
    - Display a list of coffee shops with detailed information.
    - Coffee shop rating system.

- **Profile Avatars**:
    - Upload avatars during registration and profile editing.
    - Display avatars on user lists and profile pages.

- **UI/UX Enhancements**:
    - Responsive design with a dark theme.
    - Real-time validation for required fields and avatar uploads with file size checks.
    - Custom error pages for 404 and 500 errors.

---

## Technology Stack

- **Backend**:
    - Java with Spring Boot
    - Spring Security for authentication and authorization
    - Hibernate for ORM

- **Frontend**:
    - Thymeleaf for server-side rendering
    - HTML, CSS, JavaScript

- **Database**:
    - PostgreSQL

- **Build Tool**:
    - Maven

---

## Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/akulebyakin/coffeeshops.git
   cd coffeeshops
   ```

2. **Create application.properties**:

   - Create a copy of `application-backup.properties` and rename it to `application.properties`
   
3. **Set Up the Database**:

    - Create a PostgreSQL database.
    - Update the `application.properties` file with your database credentials:

      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      ```

4. **Run the Application**:

   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:

    - Open your browser and navigate to `http://localhost:8080`.

---

## Tests

This project includes unit and integration tests to ensure functionality.

### Running Tests

1. **Run All Tests**:

   Use Maven to run all tests:

   ```bash
   mvn test
   ```

2. **Run Specific Tests**:

   To run a specific test class:

   ```bash
   mvn -Dtest=ClassName test
   ```

---

## Usage

### Roles

- **ADMIN**:
    - Full access to user and coffee shop management: add, edit, delete.
- **MANAGER**:
    - Can add new coffee shop
    - Can edit and delete own coffee shop
- **USER**:
    - View coffee shop details.

### Avatar Upload

- Supported formats: PNG, JPEG, WebP.
- Max file size: 2 MB.

---

## Project Structure

```plaintext
src
├── main
│   ├── java/com/kulebiakin/coffeeshops
│   │   ├── controller       # Controllers for handling requests
│   │   ├── entity           # Entities for database tables
│   │   ├── repository       # JPA repositories
│   │   ├── service          # Business logic services
│   │   └── config           # Configuration files (e.g., SecurityConfig)
│   ├── resources
│   │   ├── static           # Static resources (CSS, JS)
│   │   ├── templates        # Thymeleaf templates
│   │   └── application.properties # App configuration
│   └── test                 # Unit and integration tests
```

---

## API Endpoints

### User Endpoints
- `GET /users` - List all users (ADMIN only).
- `POST /users/edit` - Edit user profile (ADMIN/USER).
- `GET /users/edit/avatar/delete/{id}` - Delete avatar (ADMIN/USER).

### Coffee Shop Endpoints
- `GET /coffee-shops` - List all coffee shops.
- `POST /coffee-shops/edit` - Edit coffee shop (ADMIN/MANAGER).

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Thymeleaf](https://www.thymeleaf.org/)
- [PostgreSQL](https://www.postgresql.org/)
