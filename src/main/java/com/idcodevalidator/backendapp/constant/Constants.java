package com.idcodevalidator.backendapp.constant;

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

    public static class RegularExpr {
        public static String ONLY_DIGITS = "[0-9]+";
        public static String ONLY_ONE_TO_SIX = "[1-6]";
        public static String BIRTH_MONTH_DIGITS_ONLY = "0[1-9]|1[0-2]";
        public static String BIRTH_DAY_DIGITS_ONLY = "0?[1-9]|[12][0-9]|3[01]";
        public static String FIRST_UP_TO_THOUSAND = "^(00[1-9]|0[1-9][0-9]|[1-9][0-9][0-9])$";
    }
}
