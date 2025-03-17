# Quiz WebApp

This project is a Java-based web application using **Spring Boot**, **Maven**, **Lombok**, and **MySQL**.

## Importing the Project into Eclipse

1. **Ensure you have Eclipse installed**.
2. **Install Lombok**:
   - Download Lombok from [Project Lombok](https://projectlombok.org/download).
   - Run the jar and specify your Eclipse installation directory.
   - Restart Eclipse after installation.
3. **Import the project**:
   - Open Eclipse.
   - Go to **File → Import**.
   - Select **Git**.
   - Select **Projects from Git**
   - Select **Clone URI**
   - Enter your credentials
   - Click **Next** and **Finish** to complete the import.
4. If you face any dependency issues, run **Maven Update**:
   - Right-click the project → **Maven** → **Update Project**.

## Running the Project

### Prerequisites
- **Lombok must be installed and configured correctly in Eclipse**.
- **Ensure a MySQL database schema named `quiz-webapp` exists**.

### Database Setup

1. Open **MySQL Workbench**.
2. Run the following command to create the required schema:
   ```sql
   CREATE DATABASE quiz-webapp;
   ```
3. Ensure your MySQL credentials are set to:
   - **Username:** `root`
   - **Password:** `root`

If you wish to change these credentials, update the following file:

📂 `src/main/resources/application.properties`
```properties
spring.datasource.username=root
spring.datasource.password=root
```

### Running the Application

1. Open the project in **Eclipse**.
2. Right-click the main class containing `@SpringBootApplication`.
3. Select **Run As → Java Application**.
4. The application should start, and you can access it at `http://localhost:8081/`.

---

