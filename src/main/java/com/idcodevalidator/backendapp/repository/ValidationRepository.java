package com.idcodevalidator.backendapp.repository;

import com.idcodevalidator.backendapp.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRepository extends JpaRepository<Validation, Long> {
}
