package com.example.rest.validation;

import com.example.rest.model.Pet;
import org.springframework.stereotype.Service;

@Service
public class PetValidation {
    public String validatePet(Pet pet) {

        if (pet == null) {
            return "Pet cannot be null";
        }

        if (pet.getId() == null || pet.getId() < 0) {
            return "ID should be positive integer number";
        }

        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            return "Name cannot be empty";
        }

        if (pet.getCategory() == null) {
            return "Category cannot be null";
        }

        if (pet.getStatus() == null) {
            return "Status cannot be null";
        }

        return "Success";
    }
}
