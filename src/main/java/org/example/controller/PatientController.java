package org.example.controller;

import org.example.dto.PatientDTO;
import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.example.repo.PatientRepo;
import org.example.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Patient")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    PatientRepo patientRepo;
    @GetMapping("/print-name")
    public Patient printName (){

     return Patient.builder().name("öo").age("21").id(12L).build();

    }

    @PostMapping("/enter-patient")
    public Patient saveName (){

        return Patient.builder().name("öko").age("21").id(64L).build();

    }

@GetMapping ("/get_patient")
    public  Patient get_Patient_By_ID (@RequestParam Long id){
        return patientService.getPatient(id);
    }

    @PostMapping ("/post_patient")
    public Patient post_Patient (@RequestBody Patient patient){
        return patientService.post_Patient(patient);
    }

    @PostMapping ("/delete_patient")
    public void delete_Patient (@RequestParam Long id){
       patientService.delete_Patient(id);
    }
    @PostMapping("/save")
    public PatientDTO save(@RequestBody PatientDTO dto) {
        Patient entity = PatientMapper.toEntity(dto);
        Patient saved = patientRepo.save(entity);
        return PatientMapper.toDTO(saved);
    }

}
