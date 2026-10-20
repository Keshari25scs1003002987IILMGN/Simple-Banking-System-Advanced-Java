package com.cyrostack.bank.exception;

/** Base class for all banking errors. */
public class BankException extends Exception {
    private static final long serialVersionUID = 1L;

    public BankException(String message) {
        super(message);
    }
}
