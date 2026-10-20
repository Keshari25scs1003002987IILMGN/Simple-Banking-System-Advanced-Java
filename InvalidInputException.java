package com.cyrostack.bank.exception;

/** Thrown when user input fails validation. */
public class InvalidInputException extends BankException {
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
