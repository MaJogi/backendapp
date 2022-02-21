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

class IdCodeValidationServiceImplTest extends UnitTestBase {

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
        createCorrectTestData(correctMonths, firstPart, lastPart, 12);
        createIncorrectTestData(incorrectMonths, firstPart, lastPart, 13, 99);

        // Actually testing with previously created test data
        assertIdCodesWithVerdict(correctMonths, Constants.CORRECT_ID);
        assertIdCodesWithVerdict(incorrectMonths, Constants.ErrorDescription.INCORRECT_BIRTH_MONTH);
    }

    @Test
    void processIdCodeIncorrectBirthDay() throws Exception {
        // String example = "39805 22 5211";
        String firstPart = "39805";
        String lastPart = "521";
        List<String> correctDays = new ArrayList<>();
        List<String> incorrectDays = new ArrayList<>();

        // Creating test data
        createCorrectTestData(correctDays, firstPart, lastPart, 31);
        createIncorrectTestData(incorrectDays, firstPart, lastPart, 32, 99);

        // Actually testing with previously created test data
        assertIdCodesWithVerdict(correctDays, Constants.CORRECT_ID);
        assertIdCodesWithVerdict(incorrectDays, Constants.ErrorDescription.INCORRECT_BIRTH_DAY);
    }

    private void createCorrectTestData(List<String> correctIdList, String firstPart,
                                       String lastPart, int amountOfCorrectIdsTo) {
        for (int i = 1; i <= amountOfCorrectIdsTo; i++) {
            if (i < 10) {
                correctIdList.add(firstPart + "0" + i + lastPart);
            } else {
                correctIdList.add(firstPart + i + lastPart);
            }
        }
    }

    private void createIncorrectTestData(List<String> incorrectIdList, String firstPart,
                                         String lastPart, int amountOfIncorrectIdsFrom, int amountOfIncorrectIdsTo) {
        for (int i = amountOfIncorrectIdsFrom; i <= amountOfIncorrectIdsTo; i++) {
            incorrectIdList.add(firstPart + i + lastPart);
        }
    }

    private void assertIdCodesWithVerdict(List<String> listOfIds, String verdictCheck) throws Exception {
        for (String id : listOfIds) {
            var controlNumber = service.calculateControlNumber(id);
            assertEquals(verdictCheck,
                    service.processIdCode(id + controlNumber)
                            .getVerdict());
        }
    }

    @Test
    void processIdCodeIncorrectBirthOrder() throws Exception {
        // String example = "3980522 521 1";
        String firstPart = "3980522";
        String lastPart = "";
        List<String> correctDays = new ArrayList<>();

        // Creating test data
        for (int i = 1; i <= 999; i++) {
            if (i < 10) {
                correctDays.add(firstPart + "00" + i + lastPart);
            } else if (i < 100) {
                correctDays.add(firstPart + "0" + i + lastPart);
            } else {
                correctDays.add(firstPart + i + lastPart);
            }
        }
        // Actually testing with previously created test data
        // 001 up to 999
        for (String id : correctDays) {
            var controlNumber = service.calculateControlNumber(id);
            assertEquals(Constants.CORRECT_ID,
                    service.processIdCode(id + controlNumber)
                            .getVerdict());
        }
        // 000
        var controlNumber = service.calculateControlNumber(firstPart + "000");
        assertEquals(Constants.ErrorDescription.INCORRECT_BIRTH_ORDER,
                service.processIdCode(firstPart + "000" + controlNumber)
                        .getVerdict());
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
            assertEquals(Character.toString(id.charAt(id.length() - 1)),
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