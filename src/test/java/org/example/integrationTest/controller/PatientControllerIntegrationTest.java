package org.example.integrationTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.repo.PatientRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * FULL INTEGRATION TEST for Controller Layer.
 * --------------------------------------------------------------
 * This test loads:
 *   - Full Spring Context
 *   - Real Controller
 *   - Real Service
 *   - Real Repository
 *   - Real H2 Database
 *   - Real JSON Serialization
 *   - data.sql (initial data)
 *
 * No MockBean here. Everything is REAL.
 * --------------------------------------------------------------
 */

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;         // Real MockMvc (not mocked)

    @Autowired
    private ObjectMapper objectMapper; // JSON <-> Object converter

    @Autowired
    private PatientRepo patientRepo; // Real repository connected to H2 DB


    @BeforeEach
    void setup() {
        System.out.println("================================================");
        System.out.println("STARTING NEW CONTROLLER INTEGRATION TEST");
        System.out.println("================================================");
    }

    // ---------------------------------------------------------------------
    // TEST 1 — GET /Patient/get_patient
    // ---------------------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Integration: GET /Patient/get_patient returns real patient from DB")
    void testGetPatientIntegration() throws Exception {

        // Ensure DB has patients from data.sql
        List<Patient> patients = patientRepo.findAll();
        assertTrue(patients.size() > 0, "Database must contain initial data");

        // Perform GET request
        mockMvc.perform(get("/Patient/get_patient")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.full-name").exists());

        System.out.println("✓ GET patient returned successfully from database");
    }

    // ---------------------------------------------------------------------
    // TEST 2 — POST /Patient/post_patient
    // ---------------------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Integration: POST /Patient/post_patient should insert into DB")
    void testPostPatientIntegration() throws Exception {

        Patient newP = new Patient(null, "Mohanad", "40");

        // Convert object → JSON
        String json = objectMapper.writeValueAsString(newP);

        // Perform POST
        mockMvc.perform(post("/Patient/post_patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full-name").value("Mohanad"));

        System.out.println("✓ POST added new patient to DB successfully");
    }

    // ---------------------------------------------------------------------
    // TEST 3 — POST /Patient/delete_patient
    // ---------------------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Integration: POST /Patient/delete_patient deletes a real patient")
    void testDeletePatientIntegration() throws Exception {

        // Insert a patient to delete
        Patient p = patientRepo.save(new Patient(null, "ToDelete", "99"));
        Long id = p.getId();

        // Perform DELETE
        mockMvc.perform(post("/Patient/delete_patient")
                        .param("id", id.toString()))
                .andExpect(status().isOk());

        // Verify deletion
        assertFalse(patientRepo.findById(id).isPresent());

        System.out.println("✓ Patient deleted successfully from DB");
    }

    // ---------------------------------------------------------------------
    // TEST 4 — POST /Patient/save (DTO)
    // ---------------------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Integration: POST /Patient/save saves DTO and returns DTO")
    void testSaveDTOIntegration() throws Exception {

        PatientDTO dto = PatientDTO.builder()
                .name("Sara")
                .age("25")
                .build();

        String jsonDto = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/Patient/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sara"))
                .andExpect(jsonPath("$.age").value("25"));

        System.out.println("✓ DTO saved & returned successfully");
    }
}
