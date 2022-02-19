package com.idcodevalidator.backendapp.unit.service;

import com.idcodevalidator.backendapp.service.IdCodeService;
import com.idcodevalidator.backendapp.unit.UnitTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class IdCodeServiceImplTest extends UnitTestBase {

    @Autowired
    IdCodeService service;

    @Test
    void processIdCodeCorrectCode() {
        assertNotNull(service);
    }

    @Test
    void calculateControlNumber() {
    }
}