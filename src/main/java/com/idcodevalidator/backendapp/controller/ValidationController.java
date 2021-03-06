package com.idcodevalidator.backendapp.controller;

import com.idcodevalidator.backendapp.entity.ValidationResult;
import com.idcodevalidator.backendapp.repository.ValidationResultRepository;
import com.idcodevalidator.backendapp.service.IdCodeValidationService;
import com.idcodevalidator.backendapp.util.payload.request.ValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class to handle ID code validations in this application.
 */
@RestController
@RequestMapping("validation")
@RequiredArgsConstructor
public class ValidationController {
    private final ValidationResultRepository validationResultRepository;

    private final IdCodeValidationService service;

    @GetMapping()
    public List<ValidationResult> getValidations() {
        return service.getValidations();
    }

    @PostMapping(value = "processIdCode"/*, produces = "application/json", consumes = "application/json"*/)
    public ResponseEntity<ValidationResult> onIdSubmit(@RequestBody final ValidationRequest processRequest) throws Exception {
        if (processRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ValidationResult result = service.processIdCode(processRequest.getIdCode());
        service.persistValidation(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
