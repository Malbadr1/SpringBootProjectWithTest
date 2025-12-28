

---

# 🧪 **Testing Documentation — Patients Spring Boot Project**

This project implements a complete multi-layer testing strategy following the **Testing Pyramid Model**.
It includes:

* Unit Tests
* Controller Tests (WebMvc)
* Repository Integration Tests
* Controller Integration Tests
* Full Flow (End-to-End) Tests
* Postman API Tests
* Newman Automated API Tests

This README explains everything related **only to testing**.

---

# 📌 **1. Testing Overview**

The project uses multiple testing layers to guarantee reliability:

| Test Type                   | What It Covers                 | Tools Used                |
| --------------------------- | ------------------------------ | ------------------------- |
| Unit Test                   | Service logic only             | JUnit 5 + Mockito         |
| WebMvc Test                 | Controller only (no DB)        | MockMvc + @WebMvcTest     |
| Repository Integration Test | Repository + H2 Database       | @DataJpaTest              |
| Controller Integration Test | Full Controller + Service + DB | @SpringBootTest + MockMvc |
| Full Flow Test              | Complete CRUD E2E              | SpringBoot + H2 + MockMvc |
| Postman Test                | Manual / automated API testing | Postman                   |
| Newman Test                 | CLI automated API testing      | Newman + Maven            |

---

# 📂 **2. Test Folder Structure**

```
src/test/java/org/example/
 ├── unitTest/
 │     ├── service/PatientServiceTest.java
 │     └── controller/PatientControllerWebMvcTest.java
 │
 ├── integrationTest/
 │     ├── repository/PatientRepoIntegrationTest.java
 │     └── controller/PatientControllerIntegrationTest.java
 │
 └── fullflow/
       └── PatientFullFlowTest.java
```

Each folder contains a different layer of the testing pyramid.

---

# 🟦 **3. Unit Testing (Service Layer)**

Purpose:

* Test **only the business logic** inside the service.
* Repository is mocked → no DB calls.
* You verify:

    * getPatient
    * post_Patient
    * delete_Patient
    * DTO ↔ Entity mapping

Tools:

* `@ExtendWith(MockitoExtension.class)`
* `@InjectMocks`
* `@Mock`
* Assertions via JUnit 5

This keeps the test **fast, isolated, and deterministic**.

---

# 🟧 **4. WebMvc Test (Controller Only)**

Purpose:

* Test the controller layer without loading the database.
* Validate:

    * Endpoints
    * JSON responses
    * Status codes
    * Request/response mapping

Tools:

* `@WebMvcTest(PatientController.class)`
* `MockMvc`
* `@MockBean` for Service + Repo

This ensures your REST API contract works as expected.

---

# 🟩 **5. Repository Integration Test (H2 Database)**

Purpose:

* Use a **real in-memory DB (H2)**.
* Validate:

    * JPA queries
    * CRUD operations
    * data.sql loading

Tools:

* `@DataJpaTest`
* `@ActiveProfiles("test")`

This verifies that your entity mapping and repository logic actually work in a real database environment.

---

# 🟫 **6. Controller Integration Test**

Purpose:

* Boot the full application context.
* Test complete path:
  **Controller → Service → Repository → Database**

Tools:

* `@SpringBootTest`
* `@AutoConfigureMockMvc`
* H2 Database

Validates the system as a whole, without mocks.

---

# 🟪 **7. Full Flow Test (End-to-End)**

This is the highest level test.
It simulates a **real user scenario**:

### Flow:

1. POST → Create Patient
2. GET → Retrieve Patient
3. PUT → Update (optional version)
4. DELETE → Remove Patient
5. GET → Ensure patient is gone

Tools:

* Full Spring Boot application
* H2 DB
* MockMvc
* JSON parsing via ObjectMapper

This test confirms that **the entire system works together seamlessly**, including database persistence.

---

# 🟥 **8. Postman Testing (Manual/API)**

You can test all endpoints manually using Postman:

Endpoints to include:

* `POST /Patient/post_patient`
* `GET /Patient/get_patient?id=X`
* `POST /Patient/delete_patient?id=X`
* Full Flow requests

Create a Postman Collection named:

```
Patients-API-Collection.json
```

---

# 🟨 **9. Newman Automated API Testing**

Run API tests automatically from the command line.

### Install Newman:

```
npm install -g newman
```

### Run Collection:

```
newman run Patients-API-Collection.json
```

### Run via Maven (optional):

Add to `pom.xml`:

```xml
<plugin>
    <groupId>com.github.sigil-eu</groupId>
    <artifactId>newman-maven-plugin</artifactId>
    <version>1.2.2</version>
    <executions>
        <execution>
            <goals><goal>run</goal></goals>
        </execution>
    </executions>
    <configuration>
        <collection>Patients-API-Collection.json</collection>
    </configuration>
</plugin>
```

Run:

```
mvn newman:run
```

---

# 🏃 **10. Running All Tests**

### Run all tests (Maven):

```
mvn test
```

### Run from IntelliJ:

Right-click the **test folder** → *Run Tests*

---

# 📈 **11. Summary**

This project implements a **complete testing architecture**:

| Layer               | Tested By                    |
| ------------------- | ---------------------------- |
| Business Logic      | Unit Tests                   |
| API Layer           | WebMvc Tests                 |
| Persistence         | JPA Integration Tests        |
| Entire System       | Controller Integration Tests |
| End-to-End Behavior | Full Flow Tests              |
| Manual API          | Postman                      |
| Automated API       | Newman                       |

This structure is **professional**, **scalable**, and **industry-standard**.

---

# 🎯 **12. Final Notes**

This README contains:

✔ Full explanation of the testing strategy
✔ How to run each test type
✔ Tools used at every layer
✔ Professional best practices

If you want:

* Swagger documentation
* Jacoco coverage report
* GitHub Actions CI pipeline
* Architecture diagrams

I can add them.

Just say **"Add Swagger"** or **"Add CI/CD"** and I will continue.
