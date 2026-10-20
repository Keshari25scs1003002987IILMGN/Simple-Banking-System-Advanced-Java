package com.cyrostack.bank.exception;

/** Thrown on wrong credentials or a locked account. */
public class AuthenticationException extends BankException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
