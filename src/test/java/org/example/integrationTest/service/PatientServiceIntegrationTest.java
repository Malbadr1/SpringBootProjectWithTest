package org.example.integrationTest.service;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.repo.PatientRepo;
import org.example.service.PatientService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FULL INTEGRATION TEST FOR SERVICE LAYER
 *
 * This test loads:
 * - REAL Spring Boot context
 * - REAL H2 database
 * - REAL Repository
 * - REAL Service
 * - REAL Mapper
 *
 * No Mockito, no mocks → 100% REAL environment
 */

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)  // Force H2 for testing
@Transactional          // Each test runs inside a transaction
@Rollback(true)         // Database returns to original state after each test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientServiceIntegrationTest {

    @Autowired
    private PatientService patientService;     // REAL service

    @Autowired
    private PatientRepo patientRepo;           // REAL repository connected to H2


    // -----------------------------------------------------------
    // TEST 1 — Ensure data.sql was loaded
    // -----------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Load data.sql and verify H2 contains initial records")
    void testInitialData() {

        System.out.println("TEST: testInitialData");

        List<Patient> patients = patientRepo.findAll();

        // EXPECT: data.sql inserted 3 rows
        assertEquals(3, patients.size());

        patients.forEach(System.out::println);
    }


    // -----------------------------------------------------------
    // TEST 2 — getPatient(id) returns correct patient
    // -----------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Service.getPatient(id) returns a valid patient from DB")
    void testGetPatient() {

        System.out.println("TEST: testGetPatient");

        Patient patient = patientService.getPatient(1L);

        assertNotNull(patient);
        assertEquals("Ali", patient.getName());
        assertEquals("22", patient.getAge());

        System.out.println("Returned patient = " + patient);
    }


    // -----------------------------------------------------------
    // TEST 3 — post_Patient(patient) saves entity in DB
    // -----------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Service.post_Patient() inserts a new record in DB")
    void testPostPatient() {

        System.out.println("TEST: testPostPatient");

        Patient newPatient = new Patient(null, "Mohanad", "40");

        // REAL save → will generate ID
        Patient saved = patientService.post_Patient(newPatient);

        assertNotNull(saved.getId());
        assertEquals("Mohanad", saved.getName());

        System.out.println("Saved patient = " + saved);
    }


    // -----------------------------------------------------------
    // TEST 4 — delete_Patient(id) actually deletes from DB
    // -----------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Service.delete_Patient deletes record from DB")
    void testDeletePatient() {

        System.out.println("TEST: testDeletePatient");

        // First ensure record exists
        Patient p = patientService.getPatient(1L);
        assertNotNull(p);

        // Now delete it
        patientService.delete_Patient(1L);

        // Should return null now
        Patient deleted = patientService.getPatient(1L);
        assertNull(deleted);

        System.out.println("Patient with ID 1 successfully deleted");
    }


    // -----------------------------------------------------------
    // TEST 5 — DTO mapping + service + repo combined
    // -----------------------------------------------------------
    @Test
    @Order(5)
    @DisplayName("Service.getPatientdto(id) returns DTO correctly")
    void testGetPatientDTO() {

        System.out.println("TEST: testGetPatientDTO");

        PatientDTO dto = patientService.getPatientdto(2L);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Assa", dto.getName());
        assertEquals("30", dto.getAge());

        System.out.println("DTO = " + dto);
    }
}
