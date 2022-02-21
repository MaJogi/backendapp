package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.entity.ValidationResult;

public interface IdCodeValidationService {
    ValidationResult processIdCode(String idCode) throws Exception;
    int calculateControlNumber(String idCode);
    ValidationResult createValidation(String idCode, boolean failed, String reason) throws Exception;
}
