package com.idcodevalidator.backendapp;

/**
 * Application specific constants that can be used anywhere.
 */
public class Constants {
    public static int ID_CODE_LENGTH = 11;
    public static String CORRECT_ID = "Correct ID";

    public static class ErrorDescription {
        public static String INCORRECT_CODE_LENGTH = "Incorrect ID code length";
        public static String ONLY_DIGITS_ALLOWED = "Only digits allowed";
        public static String INCORRECT_GENDER_IDENTIFIER = "Incorrect gender identifier (first) digit";
        public static String INCORRECT_BIRTH_MONTH = "Incorrect birth month (third-fourth) digits";
        public static String INCORRECT_BIRTH_DAY = "Incorrect birth day (fifth-sixth) digits";
        public static String INCORRECT_BIRTH_ORDER = "Incorrect birth order (seventh up to tenth) digits";
        public static String INCORRECT_CONTROL_NUMBER = "Incorrect control number, please check given ID code";
    }
}
