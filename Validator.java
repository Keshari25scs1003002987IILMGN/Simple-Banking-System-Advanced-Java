package com.cyrostack.bank.util;

import com.cyrostack.bank.exception.InvalidInputException;

public final class Validator {
    private Validator() {}

    public static String name(String s) throws InvalidInputException {
        if (s == null || !s.trim().matches("[A-Za-z ]{2,50}"))
            throw new InvalidInputException("Name must be 2-50 letters (spaces allowed).");
        return s.trim();
    }

    public static String phone(String s) throws InvalidInputException {
        if (s == null || !s.trim().matches("[6-9]\\d{9}"))
            throw new InvalidInputException("Phone must be a valid 10-digit Indian mobile number.");
        return s.trim();
    }

    public static String pin(String s) throws InvalidInputException {
        if (s == null || !s.matches("\\d{4}"))
            throw new InvalidInputException("PIN must be exactly 4 digits.");
        return s;
    }

    public static double amount(double a) throws InvalidInputException {
        if (Double.isNaN(a) || Double.isInfinite(a) || a <= 0)
            throw new InvalidInputException("Amount must be a positive number.");
        if (a > 1_000_000)
            throw new InvalidInputException("Single transaction limit is Rs. 10,00,000.");
        return Math.round(a * 100.0) / 100.0;
    }
}
