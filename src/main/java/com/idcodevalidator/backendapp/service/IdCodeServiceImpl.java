package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.Constants;
import com.idcodevalidator.backendapp.entity.Validation;
import com.idcodevalidator.backendapp.exception.*;
import com.idcodevalidator.backendapp.repository.ValidationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class IdCodeServiceImpl implements IdCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdCodeServiceImpl.class);

    @Autowired
    ValidationRepository repository;

    /**
     * @param idCode
     * @return
     */
    @Override
    public Validation processIdCode(String idCode) {

        /* Of course there is a chance that some day and month pair don't exist. Like days 29-31 in February.
           But I won't overcomplicate solution by trying to convert parts of ID code to date object. In real
           solution this might be actually required by functional requirements. So I made an assumption
           that none possible date isn't the case. In real application I would mark this TODO and create a
           jira issue to fix this.*/

        LOGGER.debug("Starting processing ID code");

        Validation validation;

        /* Note: Length is already checked by frontend but there is still a chance that form can be manipulated
           to bypass minLength property. Backend check might still be useful */
        if (idCode.length() != Constants.ID_CODE_LENGTH) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_CODE_LENGTH);
        } else if (!idCode.matches("[0-9]+")) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.ONLY_DIGITS_ALLOWED);
        } else if (!idCode.substring(0, 1).matches("[1-6]")) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_GENDER_IDENTIFIER);
        } else if (!idCode.substring(3, 5).matches("0[1-9]|1[0-2]")) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_BIRTH_MONTH);
        } else if (!idCode.substring(5, 7).matches("0?[1-9]|[12][0-9]|3[01]")) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_BIRTH_DAY);
        } else if (!idCode.substring(7, 10).matches("^(00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])$")) {
            validation = createValidation(idCode,
                    true, Constants.ErrorDescription.INCORRECT_BIRTH_ORDER);
        } else if (!(Integer.parseInt(idCode.substring(10, 11)) == (calculateControlNumber(idCode)))) {
            validation = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_CONTROL_NUMBER);
        } else {
            // If everything OK
            validation = createValidation(idCode, false, Constants.CORRECT_ID);
        }
        persistValidation(validation);

        return validation;
    }

    // TODO. WRITE TESTS

    /**
     * @param idCode
     * @param failed
     * @param reason
     */
    private Validation createValidation(String idCode, boolean failed, String reason) {
        var validation = new Validation();
        validation.setIdCode(idCode);
        validation.setTimeStamp(LocalDateTime.now());
        validation.setFailed(failed);
        validation.setVerdict(reason);
        return validation;
    }

    /**
     * For now, normal save operation doesn't need separate method, but perhaps more complex handling could be needed
     * in the future.
     *
     * @param validation
     */
    private void persistValidation(Validation validation) {
        repository.save(validation);
    }

    @Override
    public int calculateControlNumber(String idCode) {
        List<Integer> firstStageWeights = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 1);
        List<Integer> secondStageWeights = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 1, 2, 3);

        int sum = 0;
        for (int i = 0; i < idCode.length() - 1; i++) {
            sum += Integer.parseInt(String.valueOf(idCode.charAt(i))) * firstStageWeights.get(i); // Fixme: remove duplication
        }

        int result = sum % 11;
        // if sum is <10, we found missing number
        if (result < 10) {
            LOGGER.debug("First round control number: " + result);
        } else { // doing calculation with secondStageWeights
            sum = 0;
            for (int i = 0; i < idCode.length() - 1; i++) {
                sum += (int) idCode.charAt(i) * secondStageWeights.get(i);
            }
            result = sum % 11;
            LOGGER.debug("Second round control number: " + result);
        }

        return result;
    }
}
