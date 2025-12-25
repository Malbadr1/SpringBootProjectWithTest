package org.example.unitTset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.PatientController;
import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.repo.PatientRepo;
import org.example.service.PatientService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * FULL PROFESSIONAL MockMvc TEST
 * Testing ONLY the Controller layer (NOT the service, NOT the DB).
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;   // Mocking Service

    @MockBean
    private PatientRepo patientRepo;         // Mocking Repository (used in /save)

    @Autowired
    private ObjectMapper objectMapper;       // Converts objects ↔ JSON

    // --------------------------------------------------------
    // TEST 1 — GET /get_patient
    // --------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("GET /Patient/get_patient should return JSON of patient")
    void testGetPatient() throws Exception {

        System.out.println("TEST CASE: GET /Patient/get_patient");

        // Arrange
        Patient fake = new Patient(1L, "Ali", "22");
        when(patientService.getPatient(1L)).thenReturn(fake);

        // Act + Assert
        mockMvc.perform(get("/Patient/get_patient")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full-name").value("Ali"))
                .andExpect(jsonPath("$.age").value("22"));

        verify(patientService, times(1)).getPatient(1L);
    }

    // --------------------------------------------------------
    // TEST 2 — POST /post_patient
    // --------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("POST /Patient/post_patient should save patient")
    void testPostPatient() throws Exception {

        System.out.println("TEST CASE: POST /Patient/post_patient");

        Patient input = new Patient(null, "Mohanad", "40");
        Patient saved = new Patient(10L, "Mohanad", "40");

        when(patientService.post_Patient(Mockito.any(Patient.class))).thenReturn(saved);

        mockMvc.perform(post("/Patient/post_patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.full-name").value("Mohanad"));

        verify(patientService, times(1)).post_Patient(Mockito.any(Patient.class));
    }

    // --------------------------------------------------------
    // TEST 3 — POST /delete_patient
    // --------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("POST /Patient/delete_patient should call service delete")
    void testDeletePatient() throws Exception {

        System.out.println("TEST CASE: POST /Patient/delete_patient");

        Long id = 5L;
        doNothing().when(patientService).delete_Patient(id);

        mockMvc.perform(post("/Patient/delete_patient")
                        .param("id", "5"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).delete_Patient(id);
    }

    // --------------------------------------------------------
    // TEST 4 — POST /save → DTO + Mapper + Repo
    // --------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("POST /Patient/save should save DTO and return DTO")
    void testSaveDTO() throws Exception {

        System.out.println("TEST CASE: POST /Patient/save");

        // Input DTO
        PatientDTO dto = PatientDTO.builder()
                .id(100L)
                .name("Sara")
                .age("25")
                .build();

        // Entity result saved in DB
        Patient entity = new Patient(100L, "Sara", "25");

        when(patientRepo.save(any(Patient.class))).thenReturn(entity);

        mockMvc.perform(post("/Patient/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("Sara"))
                .andExpect(jsonPath("$.age").value("25"));

        verify(patientRepo, times(1)).save(any(Patient.class));
    }
}
