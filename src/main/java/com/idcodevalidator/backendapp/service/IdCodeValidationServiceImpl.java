package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.constant.Constants;
import com.idcodevalidator.backendapp.entity.ValidationResult;
import com.idcodevalidator.backendapp.repository.ValidationResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation class for checking validity of Estonian IDs.
 */
@Service
@RequiredArgsConstructor
public class IdCodeValidationServiceImpl implements IdCodeValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdCodeValidationServiceImpl.class);

    private final ValidationResultRepository validationResultRepository;

    /**
     * Main method, which checks validity of Estonian identity code by checking
     * specific digits on specific location.
     *
     * @param idCode Estonian identity code.
     * @return Processed Validation that is already added to database.
     */
    @Override
    public ValidationResult processIdCode(String idCode) throws Exception {

        /* Of course there is a chance that some day and month pair don't exist. Like days 29-31 in February.
           But I won't overcomplicate solution by trying to convert parts of ID code to date object. In real
           solution this might be actually required by functional requirements. So I made an assumption
           that none possible date isn't the case. In real application I would mark this TODO and create a
           jira issue to fix this.*/

        LOGGER.debug("Starting processing ID code");

        ValidationResult validationResult;

        /* Note: Length is already checked by frontend but there is still a chance that form can be manipulated
           to bypass minLength property. Backend check might still be useful */
        if (idCode.length() != Constants.ID_CODE_LENGTH) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_CODE_LENGTH);
        } else if (!idCode.matches(Constants.RegularExpr.ONLY_DIGITS)) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.ONLY_DIGITS_ALLOWED);
        } else if (!idCode.substring(0, 1).matches(Constants.RegularExpr.ONLY_ONE_TO_SIX)) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_GENDER_IDENTIFIER);
        } else if (!idCode.substring(3, 5).matches(Constants.RegularExpr.BIRTH_MONTH_DIGITS_ONLY)) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_BIRTH_MONTH);
        } else if (!idCode.substring(5, 7).matches(Constants.RegularExpr.BIRTH_DAY_DIGITS_ONLY)) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_BIRTH_DAY);
        } else if (!idCode.substring(7, 10).matches(Constants.RegularExpr.FIRST_UP_TO_THOUSAND)) {
            validationResult = createValidation(idCode,
                    true, Constants.ErrorDescription.INCORRECT_BIRTH_ORDER);
        } else if (!(Integer.parseInt(idCode.substring(10, 11)) == (calculateControlNumber(idCode)))) {
            validationResult = createValidation(idCode, true,
                    Constants.ErrorDescription.INCORRECT_CONTROL_NUMBER);
        } else {
            // If everything OK
            validationResult = createValidation(idCode, false, Constants.CORRECT_ID);
        }

        return validationResult;
    }

    /**
     * Method creates Validation object, which will be saved to database later on.
     *
     * @param idCode Estonian identity code.
     * @param failed True, if ID code is incorrect.
     * @param reason Exact written out reason for successful or unsuccessful validation.
     */
    public ValidationResult createValidation(String idCode, boolean failed, String reason) throws Exception {
        if (idCode == null || reason == null) {
            throw new Exception("Method arguments cannot be null"); //Todo. Maybe add custom exception.
        }

        var validation = new ValidationResult();
        validation.setIdCode(idCode);
        validation.setTimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        validation.setFailed(failed);
        validation.setVerdict(reason);
        return validation;
    }

    /**
     * For now, normal save operation doesn't need separate method, but perhaps more complex handling could be needed
     * in the future.
     *
     * @param validationResult Validation object which would be added to database.
     */
    public void persistValidation(ValidationResult validationResult) {
        validationResultRepository.save(validationResult);
    }

    /**
     *
     * @param idCode Estonian identity code
     * @return Control number to be added at the end of the id code.
     */
    public int calculateControlNumber(String idCode) {
        List<Integer> firstStageWeights = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 1);
        List<Integer> secondStageWeights = Arrays.asList(3, 4, 5, 6, 7, 8, 9, 1, 2, 3);

        var idCodeLength = 11;

        int sum = 0;
        for (int i = 0; i < idCodeLength - 1; i++) {
            sum += Integer.parseInt(Character.toString(idCode.charAt(i))) * firstStageWeights.get(i); // Fixme: remove duplication
        }

        int result = sum % 11;

        // if sum is < 10, we found missing number
        if (result < 10) {
            LOGGER.debug("First round control number: " + result);
        } else { // doing calculation with secondStageWeights
            sum = 0;
            for (int i = 0; i < idCodeLength - 1; i++) {
                sum += Integer.parseInt(Character.toString(idCode.charAt(i))) * secondStageWeights.get(i);
            }
            result = sum % 11;
            if (result == 10) { // last possible value
                return 0;
            }
            LOGGER.debug("Second round control number: " + result);
        }

        return result;
    }

    public List<ValidationResult> getValidations() {
        return this.validationResultRepository.findAll();
    }
}
