package com.cyrostack.bank.model;

import com.cyrostack.bank.exception.InsufficientFundsException;
import com.cyrostack.bank.util.SecurityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int MAX_FAILED_ATTEMPTS = 3;

    private final String accountNumber;
    private final String holderName;
    private final String phone;
    private final String salt = SecurityUtil.newSalt();
    private String pinHash;
    protected double balance;
    private int failedAttempts;
    private boolean locked;
    private final ArrayList<Transaction> history = new ArrayList<>();

    protected Account(String accountNumber, String holderName, String phone, String pin, double openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.phone = phone;
        this.pinHash = SecurityUtil.hash(pin, salt);
        this.balance = openingBalance;
        record(TransactionType.OPENING, openingBalance);
    }

    /** Maximum amount that can leave this account right now. */
    public abstract double availableToWithdraw();
    public abstract String getType();

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
    public boolean isLocked() { return locked; }
    public List<Transaction> getHistory() { return Collections.unmodifiableList(history); }

    public boolean checkPin(String pin) {
        return pinHash.equals(SecurityUtil.hash(pin, salt));
    }

    public void changePin(String newPin) {
        this.pinHash = SecurityUtil.hash(newPin, salt);
    }

    public void registerFailedAttempt() {
        if (++failedAttempts >= MAX_FAILED_ATTEMPTS) locked = true;
    }

    public int attemptsLeft() { return Math.max(0, MAX_FAILED_ATTEMPTS - failedAttempts); }
    public void resetFailedAttempts() { failedAttempts = 0; }
    public void unlock() { locked = false; failedAttempts = 0; }

    public void deposit(double amount, TransactionType type) {
        balance += amount;
        record(type, amount);
    }

    public void withdraw(double amount, TransactionType type) throws InsufficientFundsException {
        if (amount > availableToWithdraw())
            throw new InsufficientFundsException(String.format(
                    "Insufficient funds. You can withdraw up to Rs. %.2f.", availableToWithdraw()));
        balance -= amount;
        record(type, amount);
    }

    private void record(TransactionType type, double amount) {
        history.add(new Transaction(type, amount, balance));
    }
}
