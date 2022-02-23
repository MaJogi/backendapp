package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.entity.ValidationResult;

import java.util.List;

public interface IdCodeValidationService {
    ValidationResult processIdCode(String idCode) throws Exception;
    int calculateControlNumber(String idCode);
    ValidationResult createValidation(String idCode, boolean failed, String reason) throws Exception;
    void persistValidation(ValidationResult validationResult);
    List<ValidationResult> getValidations();
}
