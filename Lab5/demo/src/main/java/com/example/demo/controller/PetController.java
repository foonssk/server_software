package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.service.PetService;
import com.example.demo.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/lab5")
public class PetController {
    private static final Logger LOGGER = Logger.getLogger(PetController.class.getName());
    private final PetService petService;
    private final Validation validation;

    @Autowired
    public PetController(PetService petService, Validation validation) {
        this.petService = petService;
        this.validation = validation;
    }

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(@RequestBody Pet pet) {
        String validationResult = validation.validatePet(pet);
        if (!"Success".equals(validationResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Ошибка валидации: " + validationResult);
        }

        try {
            Pet createdPet = petService.create(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
        } catch (Exception e) {
            LOGGER.warning("Ошибка при добавлении питомца: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось создать питомца: " + e.getMessage());
        }
    }

    @PutMapping("/pet")
    public ResponseEntity<?> updatePet(@RequestBody Pet pet) {
        String validationResult = validation.validatePet(pet);
        if (!"Success".equals(validationResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Ошибка валидации: " + validationResult);
        }

        try {
            Pet updatedPet = petService.update(pet);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            LOGGER.warning("Ошибка при обновлении питомца: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Питомец не найден");
        }
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        try {
            Pet pet = petService.findById(petId);
            return ResponseEntity.ok(pet);
        } catch (RuntimeException e) {
            LOGGER.warning("Попытка получить питомца, которого нет: ID " + petId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Питомец не найден");
        }
    }

    @GetMapping("/pet")
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.findAllPets();
        if (pets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(pets);
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            petService.deletePet(petId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.warning("Ошибка при удалении питомца: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Питомец не найден");
        }
    }

    @PostMapping("/pet/{petId}")
    public ResponseEntity<?> updatePetWithForm(
            @PathVariable Long petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Status status
    ) {
        try {
            Pet updatedPet = petService.updateById(petId, name, status);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            LOGGER.warning("Ошибка при обновлении питомца через форму: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при обработке данных: " + e.getMessage());
        }
    }
}
