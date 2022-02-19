package com.idcodevalidator.backendapp.service;

import com.idcodevalidator.backendapp.entity.Validation;

public interface IdCodeService {
    Validation processIdCode(String idCode);
    int calculateControlNumber(String idCode);
    //Fixme: add other methods from implementation here
}
