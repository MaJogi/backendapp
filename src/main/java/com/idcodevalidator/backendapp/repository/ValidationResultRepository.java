package com.idcodevalidator.backendapp.repository;

import com.idcodevalidator.backendapp.entity.ValidationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationResultRepository extends JpaRepository<ValidationResult, Long> {
}
