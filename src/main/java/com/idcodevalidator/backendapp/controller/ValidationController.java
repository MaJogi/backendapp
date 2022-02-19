package com.idcodevalidator.backendapp.controller;

import com.idcodevalidator.backendapp.entity.Validation;
import com.idcodevalidator.backendapp.exception.*;
import com.idcodevalidator.backendapp.repository.ValidationRepository;
import com.idcodevalidator.backendapp.service.IdCodeService;
import com.idcodevalidator.backendapp.util.payload.request.ValidationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class ValidationController {
    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    IdCodeService service;

    @GetMapping("validations")
    public List<Validation> getValidations() {
        return this.validationRepository.findAll();
    }

    @PostMapping(value = "processIdCode", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> onIdSubmit(@RequestBody final ValidationRequest processRequest) {
        Validation result = service.processIdCode(processRequest.getIdCode());
        return new ResponseEntity<>(result.getVerdict(), HttpStatus.OK);
    }
}
