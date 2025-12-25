package org.example.unitTset.service;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.repo.PatientRepo;
import org.example.service.PatientService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for PatientService
 * Using JUnit 5 + Mockito
 *
 * This class tests ONLY the service layer (NOT controller, NOT database).
 * We mock PatientRepo to isolate the logic inside PatientService.
 */

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)   // Order tests
class PatientServiceUnitTest {

    @Mock
    private PatientRepo patientRepo;   // Fake repo that Spring will not use

    @InjectMocks
    private PatientService patientService;   // Class under test

    @BeforeEach
    void before() {
        System.out.println("------------------------------------------------");
        System.out.println("STARTING NEW TEST CASE");
        System.out.println("------------------------------------------------");
    }

    @AfterEach
    void after() {
        System.out.println("------------------------------------------------");
        System.out.println("TEST CASE FINISHED");
        System.out.println("================================================\n");
    }

    // =====================================================================
    // TEST 1: getPatient(id) - when patient exists
    // =====================================================================
    @Test
    @Order(1)
    @DisplayName("Test: getPatient(id) returns valid patient")
    void testGetPatientById() {

        System.out.println("TEST CASE: testGetPatientById");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        Patient fake = new Patient(1L, "Ali", "22");

        // Mocking repository behavior
        when(patientRepo.findById(1L)).thenReturn(Optional.of(fake));

        // ACT
        System.out.println("STEP 2: ACT");
        Patient result = patientService.getPatient(1L);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        assertNotNull(result, "Service should NOT return null");
        assertEquals("Ali", result.getName(), "Names must match");

        // VERIFY
        System.out.println("STEP 4: VERIFY Calls");
        verify(patientRepo, times(1)).findById(1L);
    }

    // =====================================================================
    // TEST 2: getPatient(id) - when patient does NOT exist
    // =====================================================================
    @Test
    @Order(2)
    @DisplayName("Test: getPatient(id) returns null when not found")
    void testGetPatient_NotFound() {

        System.out.println("TEST CASE: testGetPatient_NotFound");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        when(patientRepo.findById(99L)).thenReturn(Optional.empty());

        // ACT
        System.out.println("STEP 2: ACT");
        Patient result = patientService.getPatient(99L);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        assertNull(result, "Service should return null for non-existing ID");

        // VERIFY
        verify(patientRepo, times(1)).findById(99L);
    }

    // =====================================================================
    // TEST 3: post_Patient(patient) - save patient
    // =====================================================================
    @Test
    @Order(3)
    @DisplayName("Test: post_Patient(patient) saves patient")
    void testSavePatient() {

        System.out.println("TEST CASE: testSavePatient");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        Patient input = new Patient(null, "Mohanad", "40");
        Patient saved = new Patient(10L, "Mohanad", "40");

        // Mock save behavior
        when(patientRepo.save(input)).thenReturn(saved);

        // ACT
        System.out.println("STEP 2: ACT");
        Patient result = patientService.post_Patient(input);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        assertEquals(10L, result.getId());
        assertEquals("Mohanad", result.getName());

        // VERIFY
        verify(patientRepo, times(1)).save(input);
    }

    // =====================================================================
    // TEST 4: delete_Patient(id)
    // =====================================================================
    @Test
    @Order(4)
    @DisplayName("Test: delete_Patient(id) should delete patient by ID")
    void testDeletePatient() {

        System.out.println("TEST CASE: testDeletePatient");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        Long idToDelete = 5L;

        // deleteById() returns void → must use doNothing()
        doNothing().when(patientRepo).deleteById(idToDelete);

        // ACT
        System.out.println("STEP 2: ACT");
        patientService.delete_Patient(idToDelete);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        verify(patientRepo, times(1)).deleteById(idToDelete);

        // Extra safety: Ensure no exception thrown
        assertDoesNotThrow(() -> patientService.delete_Patient(idToDelete));
    }

    // =====================================================================
    // TEST 5: getPatientdto(id) - DTO mapping test
    // =====================================================================
    @Test
    @Order(5)
    @DisplayName("Test: getPatientdto(id) returns DTO mapped from entity")
    void testGetPatientDto() {

        System.out.println("TEST CASE: testGetPatientDto");

        // STEP 1: ARRANGE
        Long id = 10L;

        // Fake entity
        Patient fakeEntity = Patient.builder()
                .id(id)
                .name("Ali")
                .age("22")
                .build();

        // Mock repo behavior
        when(patientRepo.findById(id)).thenReturn(Optional.of(fakeEntity));

        // STEP 2: ACT
        PatientDTO result = patientService.getPatientdto(id);

        // STEP 3: ASSERT
        assertNotNull(result, "DTO must NOT be null");
        assertEquals(id, result.getId());
        assertEquals("Ali", result.getName());
        assertEquals("22", result.getAge());

        // STEP 4: VERIFY
        verify(patientRepo, times(1)).findById(id);
    }

}
