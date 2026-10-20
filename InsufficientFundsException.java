package com.cyrostack.bank.exception;

/** Thrown when a withdrawal or transfer exceeds the available funds. */
public class InsufficientFundsException extends BankException {
    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String message) {
        super(message);
    }
}
