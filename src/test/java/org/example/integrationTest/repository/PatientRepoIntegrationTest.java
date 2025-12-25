package org.example.integrationTest.repository;

import org.example.entity.Patient;
import org.example.repo.PatientRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRATION TEST: PatientRepo + H2 + JPA
 *
 * This test loads ONLY the Repository layer with a real in-memory database (H2)
 * No mocks are used. Hibernate will create the table "patients", and data.sql is loaded.
 */

@DataJpaTest                // Loads ONLY JPA + Repository + H2 Database
@ActiveProfiles("test")      // Optional profile for test configs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientRepoIntegrationTest {

    @Autowired
    private PatientRepo patientRepo;     // Real repository connected to H2 DB

    // -----------------------------------------------------------
    // TEST 1 → Ensure data.sql is loaded correctly
    // -----------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Load initial data.sql and ensure records exist")
    void testInitialDataLoaded() {

        System.out.println("TEST: testInitialDataLoaded");

        List<Patient> patients = patientRepo.findAll();

        // Assert
        assertEquals(3, patients.size(), "data.sql should insert 3 records");

        System.out.println("Loaded Patients:");
        patients.forEach(System.out::println);
    }

    // -----------------------------------------------------------
    // TEST 2 → Save new patient into real DB
    // -----------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Save new patient into H2 database")
    void testSavePatient() {

        System.out.println("TEST: testSavePatient");

        Patient p = new Patient(null, "Mohanad", "40");

        Patient saved = patientRepo.save(p);

        assertNotNull(saved.getId(), "ID must be auto-generated");
        assertEquals("Mohanad", saved.getName());

        System.out.println("Saved patient → " + saved);
    }

    // -----------------------------------------------------------
    // TEST 3 → Find patient by ID
    // -----------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Find patient by ID using real JPA")
    void testFindById() {

        System.out.println("TEST: testFindById");

        Patient patient = patientRepo.findById(1L).orElse(null);

        assertNotNull(patient, "Patient with ID=1 should exist");
        assertEquals("Ali", patient.getName());

        System.out.println("Found: " + patient);
    }

    // -----------------------------------------------------------
    // TEST 4 → Delete patient
    // -----------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Delete patient using real database")
    void testDeletePatient() {

        System.out.println("TEST: testDeletePatient");

        patientRepo.deleteById(2L);

        boolean exists = patientRepo.existsById(2L);

        assertFalse(exists, "Patient should be deleted");

        System.out.println("Patient with ID=2 deleted successfully");
    }
}
