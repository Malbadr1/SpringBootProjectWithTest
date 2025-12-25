package org.example.mapper;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;

public class PatientMapper {

    // Convert Entity -> DTO
    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        return PatientDTO.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .build();
    }

    // Convert DTO -> Entity
    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        return Patient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .build();
    }
}
