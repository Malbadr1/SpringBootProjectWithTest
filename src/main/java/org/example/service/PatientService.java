package org.example.service;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.example.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    // Fake database inside the service
    private List<Patient> patients = List.of(
            new Patient(1L, "Ali", "22"),
            new Patient(2L, "Assa", "30"),
            new Patient(3L, "Md", "40")
    );

    public Patient printName(String name) {

        Patient patient = patients
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findAny()
                .orElse(null);

        if (patient == null) {
            return new Patient(null, "NOTHING", null);
        }

        patient.setName(patient.getName().toUpperCase());
        return patient;
    }

    @Autowired
    private PatientRepo patientRepo;


    public Patient getPatient (Long id){
        Optional<Patient> patient= patientRepo.findById(id);
        return patient.orElse(null);
    }

    public  Patient post_Patient( Patient patient){

         patient = this.patientRepo.save(patient);
         return patient;
    }


    public void delete_Patient(Long id) {
         patientRepo.deleteById(id);

    }
    public PatientDTO getPatientdto(Long id) {
        Patient patient = patientRepo.findById(id).orElse(null);
        return PatientMapper.toDTO(patient);
    }

    public PatientDTO save(PatientDTO dto) {

        // DTO → Entity
        Patient patientEntity = PatientMapper.toEntity(dto);

        // Save in DB
        Patient saved = patientRepo.save(patientEntity);

        // Entity → DTO
        return PatientMapper.toDTO(saved);
    }

}
