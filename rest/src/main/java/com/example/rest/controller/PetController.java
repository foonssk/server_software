package com.example.rest.controller;

import com.example.rest.model.Pet;
import com.example.rest.model.Status;
import com.example.rest.service.PetService;
import com.example.rest.validation.PetValidation;
import com.example.rest.model.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/lab4")
public class PetController {
    private final PetService petService;
    private final PetValidation petValidation;

    @Autowired
    public PetController(PetService petService, PetValidation petValidation) {
        this.petService = petService;
        this.petValidation = petValidation;
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(@RequestBody Pet pet){
        String result = petValidation.validatePet(pet);

        if(!Objects.equals(result, "Success")){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse(422, "Validation exception", result));
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(petService.create(pet));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse(400, "Invalid input", e.getMessage()));
        }
    }

    @PutMapping("/pet")
    public ResponseEntity<?> updatePet(@RequestBody Pet pet) {
        String result = petValidation.validatePet(pet);

        if(!Objects.equals(result, "Success")){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
        }

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(petService.update(pet));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Pet not found", e.getMessage()));
        }
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        if(petId == null || petId < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Invalid ID supplied", "ID should be positive integer number"));
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(petService.findById(petId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, "Pet not found", e.getMessage()));
        }
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<?> updatePetWithForm(
            @PathVariable Long petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Status status
    ) {
        try {
            petService.updateById(petId, name, status);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Invalid input", e.getMessage()));
        }
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            petService.deletePet(petId);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Invalid input", e.getMessage()));
        }
    }

    @GetMapping("/pet")
    public ResponseEntity<?> getPets(){
        return ResponseEntity.status(HttpStatus.OK).body(petService.findAllPets());
    }
}
