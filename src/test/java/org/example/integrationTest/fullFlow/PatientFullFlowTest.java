package org.example.integrationTest.fullFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Patient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientFullFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Order(1)
    @DisplayName("FULL FLOW → POST → GET → DELETE → GET")
    void testFullFlow() throws Exception {

        System.out.println("\n============== FULL FLOW STARTED ==============\n");

        // ------------------------------------------------------
        // 1) POST → Create
        // ------------------------------------------------------
        String inputJson = """
                {"id": null, "full-name": "Karim", "age": "33"}
                """;

        String postResponse = mockMvc.perform(post("/Patient/post_patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full-name").value("Karim"))
                .andExpect(jsonPath("$.age").value("33"))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = mapper.readTree(postResponse).get("id").asLong();
        System.out.println("Generated ID = " + id);

        // ------------------------------------------------------
        // 2) GET → Fetch
        // ------------------------------------------------------
        mockMvc.perform(get("/Patient/get_patient").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.full-name").value("Karim"))
                .andExpect(jsonPath("$.age").value("33"));

        System.out.println("GET OK: Patient found");

        // ------------------------------------------------------
        // 3) DELETE
        // ------------------------------------------------------
        mockMvc.perform(post("/Patient/delete_patient").param("id", id.toString()))
                .andExpect(status().isOk());

        System.out.println("DELETE OK: Patient removed");

        // ------------------------------------------------------
        // 4) GET → Should be empty
        // ------------------------------------------------------
        mockMvc.perform(get("/Patient/get_patient").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        System.out.println("CONFIRMED: Patient no longer exists");

        System.out.println("\n============== FULL FLOW FINISHED ==============\n");
    }
}
