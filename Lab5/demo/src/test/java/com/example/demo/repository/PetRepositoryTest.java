package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Pet;
import com.example.demo.model.Status;
import com.example.demo.model.Tag;
import com.example.demo.service.PetService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
public class PetRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("lab5")
            .withUsername("nastya")
            .withPassword("test")
            .withReuse(true);

    @BeforeAll
    static void startContainer() {
        postgres.start();
        System.out.println("Postgres URL: " + postgres.getJdbcUrl());
    }

    @AfterAll
    static void stopContainer() {
        if (postgres != null) {
            postgres.stop();
        }
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    private static Pet testPet;

    @BeforeAll
    static void setup(@Autowired PetRepository petRepository, @Autowired PetService petService) {
        System.setProperty("docker.host" , "unix:///var/run/docker.sock");
        Category category = petService.getOrCreateCategory("Dogs");
        Tag tag = petService.getOrCreateTag("Friendly");

        testPet = new Pet();
        testPet.setName("Rex");
        testPet.setCategory(category);
        testPet.setTags(List.of(tag));
        testPet.setStatus(Status.AVAILABLE);

        petRepository.save(testPet);
    }

    @Test
    void shouldSaveAndRetrievePet() {
        Optional<Pet> foundPet = petRepository.findById(testPet.getId());

        assertThat(foundPet).isPresent();
        assertThat(foundPet.get().getName()).isEqualTo("Rex");
        assertThat(foundPet.get().getCategory().getName()).isEqualTo("Dogs");
    }

    @Test
    void shouldUpdatePet() {
        Pet savedPet = testPet;

        savedPet.setName("Max");
        petRepository.save(savedPet);
        Optional<Pet> updatedPet = petRepository.findById(savedPet.getId());

        assertThat(updatedPet).isPresent();
        assertThat(updatedPet.get().getName()).isEqualTo("Max");
    }

    @Test
    void shouldDeletePet() {
        Pet savedPet = testPet;

        petRepository.deleteById(savedPet.getId());
        Optional<Pet> deletedPet = petRepository.findById(savedPet.getId());

        assertThat(deletedPet).isEmpty();
    }

    @Test
    void shouldFindPetsByStatus() {
        // Добавляем нескольких питомцев с разными статусами
        petRepository.save(new Pet("Charlie", petService.getOrCreateCategory("Cats"), List.of(petService.getOrCreateTag("Lazy")), Status.AVAILABLE));
        petRepository.save(new Pet("Bella", petService.getOrCreateCategory("Dogs"), List.of(petService.getOrCreateTag("Energetic")), Status.PENDING));
        petRepository.save(new Pet("Rocky", petService.getOrCreateCategory("Birds"), List.of(petService.getOrCreateTag("Chirpy")), Status.SOLD));

        // Достаем только AVAILABLE
        List<Pet> availablePets = petRepository.findAll().stream()
                .filter(p -> p.getStatus() == Status.AVAILABLE)
                .toList();

        assertThat(availablePets).isNotEmpty();
        assertThat(availablePets).allMatch(p -> p.getStatus() == Status.AVAILABLE);
    }


    @Test
    void shouldFindPetsByCategory() {
        Category dogsCategory = petService.getOrCreateCategory("Dogs");
        Category catsCategory = petService.getOrCreateCategory("Cats");

        petRepository.save(new Pet("Charlie", dogsCategory, List.of(petService.getOrCreateTag("Playful")), Status.AVAILABLE));
        petRepository.save(new Pet("Bella", catsCategory, List.of(petService.getOrCreateTag("Lazy")), Status.PENDING));
        petRepository.save(new Pet("Rocky", dogsCategory, List.of(petService.getOrCreateTag("Energetic")), Status.SOLD));

        // Достаем только питомцев из категории "Dogs"
        List<Pet> dogs = petRepository.findAll().stream()
                .filter(p -> p.getCategory().getName().equals("Dogs"))
                .toList();

        assertThat(dogs).isNotEmpty();
        assertThat(dogs).allMatch(p -> p.getCategory().getName().equals("Dogs"));
    }

}