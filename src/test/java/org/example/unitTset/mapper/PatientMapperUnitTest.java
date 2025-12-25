package org.example.unitTset.mapper;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for PatientMapper
 * Now WITH detailed printing for every step.
 */

class PatientMapperUnitTest {

    // =========================================================
    // TEST 1: Convert Entity → DTO
    // =========================================================
    @Test
    void testToDTO() {

        System.out.println("========== TEST: testToDTO (Entity → DTO) ==========");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        Patient entity = Patient.builder()
                .id(1L)
                .name("Ali")
                .age("22")
                .build();
        System.out.println("Created Entity: " + entity);

        // ACT
        System.out.println("STEP 2: ACT");
        PatientDTO dto = PatientMapper.toDTO(entity);
        System.out.println("Mapped DTO: " + dto);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Ali", dto.getName());
        assertEquals("22", dto.getAge());
        System.out.println("Assertion Success: DTO fields match Entity fields");

        System.out.println("=====================================================\n");
    }

    // =========================================================
    // TEST 2: Null Entity → Null DTO
    // =========================================================
    @Test
    void testToDTO_Null() {

        System.out.println("========== TEST: testToDTO_Null ==========");

        // ACT
        System.out.println("STEP 1: ACT");
        PatientDTO dto = PatientMapper.toDTO(null);
        System.out.println("Mapping result: " + dto);

        // ASSERT
        System.out.println("STEP 2: ASSERT");
        assertNull(dto);
        System.out.println("Assertion Success: Returned null as expected");

        System.out.println("=====================================================\n");
    }

    // =========================================================
    // TEST 3: Convert DTO → Entity
    // =========================================================
    @Test
    void testToEntity() {

        System.out.println("========== TEST: testToEntity (DTO → Entity) ==========");

        // ARRANGE
        System.out.println("STEP 1: ARRANGE");
        PatientDTO dto = PatientDTO.builder()
                .id(5L)
                .name("Mohanad")
                .age("40")
                .build();
        System.out.println("Created DTO: " + dto);

        // ACT
        System.out.println("STEP 2: ACT");
        Patient entity = PatientMapper.toEntity(dto);
        System.out.println("Mapped Entity: " + entity);

        // ASSERT
        System.out.println("STEP 3: ASSERT");
        assertNotNull(entity);
        assertEquals(5L, entity.getId());
        assertEquals("Mohanad", entity.getName());
        assertEquals("40", entity.getAge());
        System.out.println("Assertion Success: Entity fields match DTO fields");

        System.out.println("=====================================================\n");
    }

    // =========================================================
    // TEST 4: Null DTO → Null Entity
    // =========================================================
    @Test
    void testToEntity_Null() {

        System.out.println("========== TEST: testToEntity_Null ==========");

        // ACT
        System.out.println("STEP 1: ACT");
        Patient entity = PatientMapper.toEntity(null);
        System.out.println("Mapping result: " + entity);

        // ASSERT
        System.out.println("STEP 2: ASSERT");
        assertNull(entity);
        System.out.println("Assertion Success: Returned null as expected");

        System.out.println("=====================================================\n");
    }
}
