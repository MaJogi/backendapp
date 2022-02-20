package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.entity.Validation;

public interface IdCodeService {
    Validation processIdCode(String idCode) throws Exception;
    int calculateControlNumber(String idCode);
    Validation createValidation(String idCode, boolean failed, String reason) throws Exception;
    //Fixme: add other methods from implementation here
}
