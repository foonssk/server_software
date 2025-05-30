package com.example.demo.repository;

import com.example.demo.entities.WeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightRepository extends JpaRepository<WeightEntity, Long> {
}
