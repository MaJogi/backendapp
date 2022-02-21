package com.idcodevalidator.backendapp.unit.service;

import com.idcodevalidator.backendapp.constant.Constants;
import com.idcodevalidator.backendapp.service.IdCodeValidationService;
import com.idcodevalidator.backendapp.unit.UnitTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IdCodeValidationServiceImplTestResult extends UnitTestBase {

    @Autowired
    IdCodeValidationService service;

    @Test
    void processIdCodeIncorrectIdCodeLength() throws Exception {
        List<String> incorrectIds = Arrays.asList("1234567891", "123456789", "123456789012");
        for (String id : incorrectIds) {
            assertEquals(Constants.ErrorDescription.INCORRECT_CODE_LENGTH, service.processIdCode(id).getVerdict());
        }
    }

    @Test
    void processIdCodeOnlyDigitsAllowed() throws Exception {
        List<String> incorrectIds = Arrays.asList("39805$25211", "$/()b5f8611", "3980522521$");
        for (String id : incorrectIds) {
            assertEquals(Constants.ErrorDescription.ONLY_DIGITS_ALLOWED, service.processIdCode(id).getVerdict());
        }
    }

    @Test
    void processIdCodeIncorrectGenderIdentifier() throws Exception {
        List<String> incorrectIds = Arrays.asList("79805225211", "89805225211", "99805225211");
        for (String id : incorrectIds) {
            assertEquals(Constants.ErrorDescription.INCORRECT_GENDER_IDENTIFIER, service.processIdCode(id).getVerdict());
        }
    }

    @Test
    void processIdCodeIncorrectBirthMonth() throws Exception {
        // String example = "398 05 225211";
        String firstPart = "398";
        String lastPart = "22521";
        List<String> correctMonths = new ArrayList<>();
        List<String> incorrectMonths = new ArrayList<>();

        // Creating test data
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                correctMonths.add(firstPart + "0" + i + lastPart);
            } else {
                correctMonths.add(firstPart + i + lastPart);
            }
        }
        for (int i = 13; i <= 99; i++) {
            incorrectMonths.add(firstPart + i + lastPart);
        }

        // Actually testing with previously created test data
        for (String id : correctMonths) {
            var controlNumber = service.calculateControlNumber(id);
            assertEquals(Constants.CORRECT_ID,
                    service.processIdCode(id + controlNumber)
                            .getVerdict());
        }
        for (String id : incorrectMonths) {
            var controlNumber = service.calculateControlNumber(id);
            assertEquals(Constants.ErrorDescription.INCORRECT_BIRTH_MONTH,
                    service.processIdCode(id + controlNumber)
                            .getVerdict());
        }
    }

    @Test
    void processIdCodeCorrectCode() throws Exception {
        List<String> correctIds = Arrays.asList("39805225211", "37012222214", "37605030299"); // Fixme: Add more.
        for (String id : correctIds) {
            assertEquals(Constants.CORRECT_ID, service.processIdCode(id).getVerdict());
        }
    }

    @Test
    void calculateControlNumber() {
        List<String> correctIds = Arrays.asList("39805225211", "37012222214", "37605030299");
        for (String id : correctIds) {
            assertEquals(Character.toString(id.charAt(id.length()-1)),
                    String.valueOf(service.calculateControlNumber(id)));
        }
    }

    @Test
    void createSuccessfulValidation() throws Exception {
        var validation = service.createValidation("39805225211", false, Constants.CORRECT_ID);
        assertEquals("39805225211", validation.getIdCode());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                validation.getTimeStamp().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(Constants.CORRECT_ID, validation.getVerdict());
        assertFalse(validation.isFailed());
    }

    @Test
    void createValidationArgumentsNull() {

    }
}